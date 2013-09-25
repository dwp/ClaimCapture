package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import models.domain._

object CareYouProvide extends Controller with CachedClaim with Navigable {
  def breaksInCare(implicit claim: DigitalForm) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

  def completed = executeOnForm {implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present())
    else track(models.domain.CareYouProvide) { implicit claim => Ok(views.html.s4_care_you_provide.g12_completed()) }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present())
    else Redirect("/time-spent-abroad/normal-residence-and-current-location")
  }

  private def completedQuestionGroups(implicit claim: DigitalForm): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.CareYouProvide)
  }
}