package controllers

import play.api.mvc._
import models.view.CachedClaim

object SubmissionService extends Controller with CachedClaim {

  def submit = Action {
    request =>
      request.body.asXml.map {
        xml =>
          val submissionXml = services.submission.GGClaimSubmission(xml).createClaimSubmission
          Ok(submissionXml)
      }.getOrElse {
        BadRequest("<p>Expecting Xml data</p>")
      }
  }
}