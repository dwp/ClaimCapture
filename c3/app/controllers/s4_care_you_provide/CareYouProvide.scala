package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{BreaksInCare, Claim}

object CareYouProvide extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.CareYouProvide)

  def breaksInCare(implicit claim: Claim) = claim.questionGroup(BreaksInCare) match {
    case Some(bs: BreaksInCare) => bs
    case _ => BreaksInCare(routes.G10BreaksInCare.present())
  }

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present())
    else Ok(views.html.s4_care_you_provide.g12_completed(completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size >= 6) Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
    else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }
}