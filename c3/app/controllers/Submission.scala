package controllers

import play.api.mvc._
import models.view._
import services.submission.ClaimSubmissionService
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{http, Logger}
import services.submission.ClaimSubmission
import services.{UnavailableTransactionIdException, PostgresTransactionIdService, TransactionIdService}

object Submission extends Controller with CachedClaim {
  val idService : TransactionIdService = new PostgresTransactionIdService()
  def submit = claiming {
    implicit claim => implicit request =>
    try {
      val id = idService.generateId
      Logger.info(s"Retrieved Id : $id")
      val claimXml = ClaimSubmission(claim, id).buildDwpClaim
      Async {
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
                    Redirect(routes.ThankYou.present())
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
          case e : java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            Redirect("/error")
          }
          case e : java.lang.Exception  => {
            Logger.error(s"InternalServerError ! ${e.getMessage}")
            Redirect("/error")
          }
        }
      }    
    }
    catch {
      case e: UnavailableTransactionIdException => {
        Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
        Redirect("/error")
      }
      case e : java.lang.Exception  => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
        Redirect("/error")
      }
    }
  }

  val UNKNOWN_ERROR_CODE: Some[String] = Some("9001")
  val BAD_REQUEST_ERROR_CODE: Some[String] = Some("9002")
  val REQUEST_TIMEOUT_ERROR_CODE: Some[String] = Some("9003")
  val INTERNAL_SERVER_ERROR_CODE: Some[String] = Some("9004")
}