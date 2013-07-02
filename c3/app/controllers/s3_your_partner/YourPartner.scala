package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import play.api.templates.Html

object YourPartner extends Controller with CachedClaim {
  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <body>
            <h1>End of Sprint 2</h1>
            <h2>Completed - About You</h2>

            <ul>
              {claim.completedQuestionGroups(models.domain.AboutYou.id).map(f => <li>{f}</li>)}
            </ul>
          </body>
        </html>

      Ok(Html(outcome.toString))
  }
}