package controllers

import play.api.mvc._
import models.view._
import services.submission.{ClaimSubmission, ClaimSubmissionService}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import play.api.{http, Logger}

object Submission extends Controller with CachedClaim {
  def submit = claiming {
    implicit claim => implicit request =>

    try {
      val claimXml = ClaimSubmission(claim, "TY2TV9G").buildDwpClaim
      Async {
        ClaimSubmissionService.submitClaim(claimXml).map(
          response => {
            // What we'll really do with the response is redirect to relevant page
            response.status match {
              case http.Status.OK =>
                //Ok(response.xml).withHeaders("Content-Type" -> "application/xhtml+xml")
                Redirect(routes.ThankYou.present())
              case http.Status.BAD_REQUEST =>
                Ok(s"BAD_REQUEST : ${response.status} : ${response.toString}")
              case http.Status.REQUEST_TIMEOUT =>
                Ok(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}")
              case http.Status.INTERNAL_SERVER_ERROR =>
                Ok(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}")
              case _ =>
                Ok(s"Unexpected response ! ${response.status} : ${response.toString}")
            }
          }
        ).recover {
          case e : java.net.ConnectException => {
            Logger.error(s"ServiceUnavailable ! ${e.getMessage}")
            ServiceUnavailable(s"ServiceUnavailable ! ${e.getMessage}")
          }
          case e : java.lang.Exception  => {
            Logger.error(s"InternalServerError ! ${e.getMessage}")
            InternalServerError(s"InternalServerError ! ${e.getMessage}")
          }
        }
      }    
    }
    catch {
      case e : java.lang.Exception  => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        InternalServerError(s"InternalServerError ! ${e.getMessage}")
      }
    }
  }
}