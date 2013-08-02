package controllers.submission

import play.api.mvc.Results.Redirect
import models.domain.Claim
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.PlainResult
import services.{PostgresTransactionIdService, TransactionIdService}
import play.api.{http, Logger}
import services.submission.{ClaimSubmissionService, ClaimSubmission}
import ExecutionContext.Implicits.global

class WebServiceSubmitter extends Submitter {
  def submit(claim: Claim): Future[PlainResult] = {
    val idService: TransactionIdService = new PostgresTransactionIdService()
    val id = idService.generateId
    Logger.info(s"Retrieved Id : $id")
    val claimXml = ClaimSubmission(claim, id).buildDwpClaim
    ClaimSubmissionService.submitClaim(claimXml).map(
      response => {
        response.status match {
          case http.Status.OK =>
            val responseStr = response.body
            Logger.info(s"Received response : $responseStr")
            val responseXml = scala.xml.XML.loadString(responseStr)
            val result = (responseXml \\ "result").text
            Logger.info(s"Received result : $result")
            result match {
              case "response" => {
                idService.registerId(id, None)
                Redirect("/thankYou")
              }
              case "acknowledgement" => {
                val correlationID = (response.xml \\ "CorrelationID").text
                Redirect("/consentAndDeclaration/error").withSession("cid" -> correlationID)
              }
              case "error" => {
                val errorCode = (responseXml \\ "errorCode").text
                Logger.error(s"Received error : $result")
                idService.registerId(id, Some(errorCode))
                Redirect("/error")
              }
              case _ => {
                Logger.info(s"Received result : $result")
                idService.registerId(id, UNKNOWN_ERROR_CODE)
                Redirect("/error")
              }
            }
          case http.Status.BAD_REQUEST =>
            Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
            idService.registerId(id, BAD_REQUEST_ERROR_CODE)
            Redirect("/error")
          case http.Status.REQUEST_TIMEOUT =>
            Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
            idService.registerId(id, REQUEST_TIMEOUT_ERROR_CODE)
            Redirect("/error")
          case http.Status.INTERNAL_SERVER_ERROR =>
            Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
            idService.registerId(id, INTERNAL_SERVER_ERROR_CODE)
            Redirect("/error")
          case _ =>
            Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
            idService.registerId(id, UNKNOWN_ERROR_CODE)
            Redirect("/error")
        }
      }
    ).recover {
      case e: java.net.ConnectException => {
        Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
        Redirect("/error")
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Redirect("/error")
      }
    }
  }

  val UNKNOWN_ERROR_CODE: Some[String] = Some("9001")
  val BAD_REQUEST_ERROR_CODE: Some[String] = Some("9002")
  val REQUEST_TIMEOUT_ERROR_CODE: Some[String] = Some("9003")
  val INTERNAL_SERVER_ERROR_CODE: Some[String] = Some("9004")

}
