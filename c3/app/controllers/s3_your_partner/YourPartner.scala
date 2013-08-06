package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import play.api.templates.Html
import models.domain._
import play.api.mvc.Call
import play.api.mvc.SimpleResult

object YourPartner extends Controller with CachedClaim {
  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim, request: Request[AnyContent]): List[(QuestionGroup, Call)] = {
    claim.completedQuestionGroups(questionGroupIdentifier).map(qg => qg -> route(qg))
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s3_your_partner.g5_completed(claim.completedQuestionGroups(models.domain.YourPartner).map(qg => qg -> route(qg)))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.YourPartner).firstPage)
  }

  def whenSectionVisible(f: => SimpleResult[Html])(implicit claim: Claim) = {
    if (claim.isSectionVisible(models.domain.YourPartner)) f
    else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def yourPartner = claiming { implicit claim => implicit request =>
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

  private def route(qg: QuestionGroup) = qg.identifier match {
    case YourPartnerPersonalDetails => routes.G1YourPartnerPersonalDetails.present()
    case YourPartnerContactDetails => routes.G2YourPartnerContactDetails.present()
    case MoreAboutYourPartner => routes.G3MoreAboutYourPartner.present()
    case PersonYouCareFor => routes.G4PersonYouCareFor.present()
  }
}