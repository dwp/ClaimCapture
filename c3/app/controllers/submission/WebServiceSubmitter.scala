package controllers.submission

import play.api.mvc.Results.Redirect
import models.domain.Claim
import scala.concurrent.{Future, ExecutionContext}
import play.api.mvc.PlainResult
import services.TransactionIdService
import play.api.{http, Logger}
import services.submission.{ClaimSubmissionService, ClaimSubmission}
import ExecutionContext.Implicits.global
import com.google.inject.Inject

class WebServiceSubmitter @Inject()(idService: TransactionIdService) extends Submitter {

  def submit(claim: Claim) : Future[PlainResult] = {
    val txnId = idService.generateId
    Logger.info(s"Retrieved Id : $txnId")
    val claimXml = ClaimSubmission(claim, txnId).buildDwpClaim

    ClaimSubmissionService.submitClaim(claimXml).map(
      response => {
        idService.registerId(txnId, SUBMITTED)
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
                Redirect("/thankYou").withNewSession
              }
              case "acknowledgement" => {
                idService.updateStatus(txnId, ACKNOWLEDGED)
                Redirect("/consentAndDeclaration/error")
              }
              case "error" => {
                val errorCode = (responseXml \\ "errorCode").text
                Logger.error(s"Received error : $result")
                errorAndCleanup(txnId, errorCode)
              }
              case _ => {
                Logger.info(s"Received result : $result")
                errorAndCleanup(txnId, UNKNOWN_ERROR)
              }
            }
          case http.Status.BAD_REQUEST =>
            Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
            errorAndCleanup(txnId, BAD_REQUEST_ERROR)
          case http.Status.REQUEST_TIMEOUT =>
            Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
            errorAndCleanup(txnId, REQUEST_TIMEOUT_ERROR)
          case http.Status.INTERNAL_SERVER_ERROR =>
            Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
            errorAndCleanup(txnId, INTERNAL_SERVER_ERROR)
          case _ =>
            Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
            errorAndCleanup(txnId, UNKNOWN_ERROR)
        }
      }
    ).recover {
      case e: java.net.ConnectException => {
        Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
        errorAndCleanup(txnId, UNKNOWN_ERROR)
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        errorAndCleanup(txnId, UNKNOWN_ERROR)
      }
    }
  }

  def errorAndCleanup(txnId: String, code: String): PlainResult = {
    idService.updateStatus(txnId, code)
    Redirect("/error").withNewSession
  }

  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val INTERNAL_SERVER_ERROR = "9004"
}
