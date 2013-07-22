package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import play.api.templates.Html
import models.domain.Claim

object YourPartner extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.YourPartner)

  def completed = claiming { implicit claim => implicit request =>
    if (claim.isSectionVisible(models.domain.YourPartner)) {
      Ok(views.html.s3_your_partner.g5_completed(completedQuestionGroups))
    }
    else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def whenVisible(claim: Claim)(closure: () => SimpleResult[Html]) = {
    val iAmVisible = claim.isSectionVisible(models.domain.YourPartner)

    if (iAmVisible) closure() else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def yourPartner = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <body>
            <h1>End of Sprint 3</h1>
            <h2>Completed - Your Partner</h2>

            <ul>
              {claim.completedQuestionGroups(models.domain.YourPartner).map(f => <li>
              {f}
            </li>)}
            </ul>
          </body>
        </html>

      Ok(Html(outcome.toString))
  }
}