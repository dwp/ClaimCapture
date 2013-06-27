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
      Async {
        val claimXml = ClaimSubmission(claim, "TY6TV9G").buildDwpClaim
        Logger.debug(s"Claim submitting transactionId : ${claimXml \\ "DWPCAClaim" \ "@id" toString()}")
        ClaimSubmissionService.submitClaim(claimXml).map(
          response => {
            // What we'll really do with the response is redirect to relevant page
            response.status match {
              case http.Status.ACCEPTED =>
                Ok(response.xml)
              case http.Status.BAD_REQUEST =>
                Ok("BAD_REQUEST")
              case http.Status.REQUEST_TIMEOUT =>
                Ok("REQUEST_TIMEOUT")
              case http.Status.INTERNAL_SERVER_ERROR =>
                Ok("INTERNAL_SERVER_ERROR")
              case _ =>
                Ok(s"Unexpected response ! ${response.status} : ${response.toString}")
            }
          }
        ).recover {
          case e : java.net.ConnectException => {
            ServiceUnavailable(e.getMessage)
          }
          case e : java.lang.Exception  => {
            ServiceUnavailable(e.getMessage)
          }
        }
      }
  }
}