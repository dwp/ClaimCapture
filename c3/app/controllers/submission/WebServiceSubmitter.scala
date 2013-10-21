package controllers.submission

import play.api.mvc.Results.Redirect
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{AnyContent, Request, PlainResult}
import play.api.{http, Logger}
import com.google.inject.Inject
import play.api.cache.Cache
import play.api.libs.ws.Response
import play.api.Play.current
import services.TransactionIdService
import services.submission.FormSubmission
import models.domain.Claim
import ExecutionContext.Implicits.global
import play.Configuration

class WebServiceSubmitter @Inject()(idService: TransactionIdService, claimSubmission : FormSubmission) extends Submitter {

  val thankYouPageUrl = Configuration.root().getString("thankyou.page")

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    retrieveRetryData(claim.key, request) match {
      case Some(retryData) => {
        claimSubmission.retryClaim(pollXml(retryData.corrId, retryData.pollUrl)).map(
          response => {
            processResponse(claim.key, retryData.txnId, response, request)
          }
        ).recover {
          case e: java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            idService.updateStatus(retryData.txnId, COMMUNICATION_ERROR)
            Redirect("/consent-and-declaration/error")
          }
          case e: java.lang.Exception => {
            Logger.error(s"InternalServerError(RETRY) ! ${e.getMessage}")
            errorAndCleanup(claim.key,retryData.txnId, UNKNOWN_ERROR)
          }
        }
      }
      case None => {
        val txnID = idService.generateId
        Logger.info(s"Retrieved Id : $txnID")

        claimSubmission.submitClaim(xml(claim, txnID)).map(
          response => {
            idService.registerId(txnID, SUBMITTED)
            processResponse(claim.key, txnID, response, request)
          }
        ).recover {
          case e: java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            idService.updateStatus(txnID, COMMUNICATION_ERROR)
            Redirect("/consent-and-declaration/error")
          }
          case e: java.lang.Exception => {
            Logger.error(s"InternalServerError(SUBMIT) ! ${e.getMessage}")
            errorAndCleanup(claim.key, txnID, UNKNOWN_ERROR)
          }
        }
      }
    }
  }

  private def processResponse(cacheKey:String, txnId: String, response: Response, request: Request[AnyContent]): PlainResult = {
    response.status match {
      case http.Status.OK =>
        val responseStr = response.body
        Logger.info(s"Received response : $responseStr")
        val responseXml = scala.xml.XML.loadString(responseStr)
        val result = (responseXml \\ "result").text
        Logger.info(s"Received result : $result")
        result match {
          case "response" => {
            idService.updateStatus(txnId, SUCCESS)
            Logger.info(s"Successful submission : $txnId")
            // Clear the cache to ensure no duplicate submission
            val key = request.session.get(cacheKey).orNull
            Cache.set(key, None)

            Redirect(thankYouPageUrl)
          }
          case "acknowledgement" => {
            idService.updateStatus(txnId, ACKNOWLEDGED)
            val correlationID = (responseXml \\ "correlationID").text
            val pollEndpoint = (responseXml \\ "pollEndpoint").text
            storeRetryData(cacheKey,RetryData(correlationID, pollEndpoint, txnId), request)
            Redirect("/consent-and-declaration/error")
          }
          case "error" => {
            val errorCode = (responseXml \\ "errorCode").text
            idService.updateStatus(txnId, errorCode)
            Logger.error(s"Received error : $result")
            Redirect("/consent-and-declaration/error")
          }
          case _ => {
            Logger.info(s"Received result : $result")
            errorAndCleanup(cacheKey,txnId, UNKNOWN_ERROR)
          }
        }
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
        errorAndCleanup(cacheKey,txnId, BAD_REQUEST_ERROR)
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
        errorAndCleanup(cacheKey,txnId, REQUEST_TIMEOUT_ERROR)
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
        errorAndCleanup(cacheKey,txnId, INTERNAL_SERVER_ERROR)
      case _ =>
        Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
        errorAndCleanup(cacheKey,txnId, UNKNOWN_ERROR)
    }
  }

  private def errorAndCleanup(cacheKey:String, txnId: String, code: String): PlainResult = {
    idService.updateStatus(txnId, code)
    Redirect(s"/error?key=$cacheKey")
  }

  case class RetryData(corrId: String, pollUrl: String, txnId: String)

  private def storeRetryData(cacheKey:String, data:RetryData, request : Request[AnyContent]) {
    val uuid = request.session.get(cacheKey)
    Cache.set(uuid+"_retry", data, 3000)
  }

  private def retrieveRetryData(cacheKey:String, request : Request[AnyContent]) : Option[RetryData] = {
    val uuid = request.session.get(cacheKey)
    Cache.getAs[RetryData](uuid+"_retry")
  }

  private[submission] def pollXml(correlationID: String, pollEndpoint: String) = {
    <poll>
      <correlationID>{correlationID}</correlationID>
      <pollEndpoint>{pollEndpoint}</pollEndpoint>
    </poll>
  }

  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val INTERNAL_SERVER_ERROR = "9004"
  val COMMUNICATION_ERROR = "9005"
}
