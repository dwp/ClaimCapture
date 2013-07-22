package controllers

import play.api.mvc._
import models.view._
import services.submission.ClaimSubmissionService
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{http, Logger}
import utils.helpers.{UnavailableTransactionIdException, UniqueTransactionId}
import services.submission.ClaimSubmission

object Submission extends Controller with CachedClaim {
  def submit = claiming {
    implicit claim => implicit request =>

    try {
      val id = UniqueTransactionId()
      Logger.info(s"Retrieved Id : $id")
      val claimXml = ClaimSubmission(claim, id).buildDwpClaim
      Async {
        ClaimSubmissionService.submitClaim(claimXml).map(
          response => {
            response.status match {
              case http.Status.OK =>
                Logger.info(s"Received response : ${response.toString}")
                // What we'll really do with the response is redirect to relevant page
                Redirect(routes.ThankYou.present())
              case http.Status.BAD_REQUEST =>
                Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}")
                Redirect("/error")
              case http.Status.REQUEST_TIMEOUT =>
                Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
                Redirect("/error")
              case http.Status.INTERNAL_SERVER_ERROR =>
                Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
                Redirect("/error")
              case _ =>
                Logger.error(s"Unexpected response ! ${response.status} : ${response.toString}")
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
}