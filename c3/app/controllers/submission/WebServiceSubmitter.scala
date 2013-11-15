package controllers.submission

import play.api.mvc.Results.Redirect
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import play.api.{http, Logger}
import com.google.inject.Inject
import play.api.cache.Cache
import play.api.Play.current
import services.TransactionIdService
import services.submission.FormSubmission
import ExecutionContext.Implicits.global
import play.Configuration
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.libs.ws.Response
import models.domain.Claim
import play.api.mvc.SimpleResult

class WebServiceSubmitter @Inject()(idService: TransactionIdService, claimSubmission: FormSubmission) extends Submitter {

  val claimThankYouPageUrl = Configuration.root().getString("claim.thankyou.page")
  val cofcThankYouPageUrl = Configuration.root().getString("cofc.thankyou.page")

  override def submit(claim: Claim, request: Request[AnyContent]): Future[PlainResult] = {
    val txnID = idService.generateId
    Logger.info(s"Retrieved Id : $txnID")

    claimSubmission.submitClaim(xml(claim, txnID)).map(
      response => {
        registerId(claim, txnID, SUBMITTED)
        processResponse(claim, txnID, response, request)
      }
    ).recover {
      case e: java.net.ConnectException => {
        Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
        updateStatus(claim, txnID, COMMUNICATION_ERROR)
        Redirect("/consent-and-declaration/error")
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError(SUBMIT) ! ${e.getMessage}")
        errorAndCleanup(claim, txnID, UNKNOWN_ERROR)
      }
    }
  }

  private def processResponse(claim: Claim, txnId: String, response: Response, request: Request[AnyContent]): PlainResult = {
    response.status match {
      case http.Status.OK =>
        val responseStr = response.body
        Logger.info(s"Received response : $claim.key : $responseStr")
        val responseXml = scala.xml.XML.loadString(responseStr)
        val result = (responseXml \\ "result").text
        Logger.info(s"Received result : $claim.key : $result")
        result match {
          case "response" => {
            updateStatus(claim, txnId, SUCCESS)
            respondWithSuccess(claim, txnId, request)
          }
          case "acknowledgement" => {
            updateStatus(claim, txnId, ACKNOWLEDGED)
            respondWithSuccess(claim, txnId, request)
          }
          case "error" => {
            val errorCode = (responseXml \\ "errorCode").text
            updateStatus(claim, txnId, errorCode)
            Logger.error(s"Received error : $result")
            Redirect("/consent-and-declaration/error")
          }
          case _ => {
            Logger.info(s"Received result : $result")
            errorAndCleanup(claim, txnId, UNKNOWN_ERROR)
          }
        }
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
        errorAndCleanup(claim, txnId, BAD_REQUEST_ERROR)
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
        errorAndCleanup(claim, txnId, REQUEST_TIMEOUT_ERROR)
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
        errorAndCleanup(claim, txnId, INTERNAL_SERVER_ERROR)
      case _ =>
        Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
        errorAndCleanup(claim, txnId, UNKNOWN_ERROR)
    }
  }


  def respondWithSuccess(claim: Claim, txnId: String, request: Request[AnyContent]): SimpleResult[Results.EmptyContent] = {
    Logger.info(s"Successful submission : $claim.key : $txnId")
    // Clear the cache to ensure no duplicate submission
    val key = request.session.get(claim.key).orNull
    Cache.set(key, None)
    claim.key match {
      case CachedClaim.key =>
        Redirect(claimThankYouPageUrl)
      case CachedChangeOfCircs.key =>
        Redirect(cofcThankYouPageUrl)
    }
  }

  private def errorAndCleanup(claim: Claim, txnId: String, code: String): PlainResult = {
    Logger.error(s"errorAndCleanup : $claim.key : $txnId : $code")
    updateStatus(claim, txnId, code)
    Redirect(controllers.routes.Application.error(claim.key))
  }

  private[submission] def pollXml(correlationID: String, pollEndpoint: String) = {
    <poll>
      <correlationID>{correlationID}</correlationID>
      <pollEndpoint>{pollEndpoint}</pollEndpoint>
    </poll>
  }

  private def updateStatus(claim: Claim, id: String, statusCode: String) = {
    idService.updateStatus(id, statusCode, claimType(claim))
  }

  private def registerId(claim: Claim, id: String, statusCode: String) = {
    idService.registerId(id, statusCode, claimType(claim))
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
