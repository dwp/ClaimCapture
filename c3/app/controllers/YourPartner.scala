package controllers

import play.api.mvc._
import models.view._
import services.submission.ClaimSubmissionService

object YourPartner extends Controller with CachedClaim {
  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <head>
            <title>Completed - About You</title>
          </head>

          <body>
            <h1>End of Sprint 1</h1>

            <ul>
              {claim.completedQuestionGroups(models.domain.AboutYou.id).map(f => <li>{f}</li>)}
            </ul>
          </body>
      </html>
      ClaimSubmissionService.submitClaim(claim)  // Temporary
      Ok(outcome)
  }
}