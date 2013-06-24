package controllers

import play.api.mvc._
import models.view._
import services.submission.ClaimSubmissionService
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object YourPartner extends Controller with CachedClaim {
  def yourPartner = claiming {
    implicit claim => implicit request =>
      Async {
        ClaimSubmissionService.submitClaim(claim).map {
          response =>
            Ok(response.xml.toString())
        }
      }
  }
}