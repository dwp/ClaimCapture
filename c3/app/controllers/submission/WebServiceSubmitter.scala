package controllers.submission

import play.api.mvc.Results.Redirect
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import play.api.{http, Logger}
import com.google.inject.Inject
import services.TransactionIdService
import services.submission.FormSubmission
import ExecutionContext.Implicits.global
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.libs.ws.Response
import models.domain.Claim


class WebServiceSubmitter @Inject()(idService: TransactionIdService, claimSubmission: FormSubmission) extends Submitter {

  override def submit(claim: Claim, request: Request[AnyContent]): Future[SimpleResult] = {
    val txnID = idService.generateId
    Logger.info(s"Retrieved Id : $txnID")

    claimSubmission.submitClaim(xml(claim, txnID)).map(
      response => {
        registerId(claim, txnID, SUBMITTED)
        processResponse(claim, txnID, response, request)
      }
    ).recover {
      case e: java.net.ConnectException =>
        Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
        errorAndCleanup(claim, txnID, COMMUNICATION_ERROR, request)
      case e: java.lang.Exception =>
        Logger.error(s"InternalServerError(SUBMIT) ! ${e.getMessage}")
        errorAndCleanup(claim, txnID, UNKNOWN_ERROR, request)
    }
  }

  private def processResponse(claim: Claim, txnId: String, response: Response, request: Request[AnyContent]): SimpleResult = {
    response.status match {
      case http.Status.OK =>
        val responseStr = response.body
        Logger.info(s"Received response : ${claim.key} : $responseStr")
        val responseXml = scala.xml.XML.loadString(responseStr)
        val status = (responseXml \\ "statusCode").text
        Logger.info(s"Received status code : $status")
        status match {
          case SUBMITTED =>
            respondWithSuccess(claim,txnId,SUCCESS)
          case _ =>
            Logger.info(s"Received result : $status")
            errorAndRedirect(claim, txnId, status)
        }
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndRedirect(claim, txnId, BAD_REQUEST_ERROR)
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndRedirect(claim, txnId, REQUEST_TIMEOUT_ERROR)
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndRedirect(claim, txnId, INTERNAL_SERVER_ERROR)
      case _ =>
        Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}, TxnId : $txnId, Headers : ${request.headers}")
        errorAndRedirect(claim, txnId, UNKNOWN_ERROR)
    }
  }

  private def respondWithSuccess(claim: Claim, txnId: String, code: String) : SimpleResult = {
    Logger.info(s"Successful submission : ${claim.key} : $txnId")
    updateStatus(claim, txnId, code)
    claim.key match {
      case CachedClaim.key =>
        Redirect(controllers.routes.ClaimEnding.thankyou())
      case CachedChangeOfCircs.key =>
        Redirect(controllers.routes.CircsEnding.thankyou())
    }
  }

  private def errorAndRedirect(claim: Claim, txnId: String, code: String): SimpleResult = {
    Logger.error(s"errorAndCleanup : ${claim.key} : $txnId : $code")
    updateStatus(claim, txnId, code)
    claim.key match {
      case CachedClaim.key => Redirect(claimErrorPageUrl)
      case CachedChangeOfCircs.key => Redirect(cofcErrorPageUrl)
    }
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
    idService.registerId(id, SUBMITTED, claimType(claim))
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
