package controllers

import play.api.mvc._
import models.view._
import services.submission.{ClaimSubmission, ClaimSubmissionService}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Submission extends Controller with CachedClaim {
  def submit = claiming {
    implicit claim => implicit request =>
      Async {
        val claimXml = ClaimSubmission(claim).buildDwpClaim
        ClaimSubmissionService.submitClaim(claimXml).map(
          response => {
            println(response.status)
            Ok(response.xml.toString())
          }
        ).recover {
          case e: java.net.ConnectException => {
            Ok(e.getMessage)
          }
        }
      }
  }
}