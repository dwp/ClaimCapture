package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import play.api.templates.Html
import models.domain._
import play.api.mvc.SimpleResult
import controllers.Routing

object YourPartner extends Controller with YourPartnerRouting with CachedClaim {
  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s3_your_partner.g5_completed(completedQuestionGroups.map(qg => qg -> route(qg)))))
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
            {completedQuestionGroups.map(f => <li>
            {f}
            </li>)}
          </ul>
        </body>
      </html>

    Ok(Html(outcome.toString))
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.YourPartner)
  }
}

trait YourPartnerRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case YourPartnerPersonalDetails => routes.G1YourPartnerPersonalDetails.present()
    case PersonYouCareFor => routes.G4PersonYouCareFor.present()
  }
}