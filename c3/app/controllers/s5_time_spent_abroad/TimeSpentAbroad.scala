package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{Trips, Claim}

object TimeSpentAbroad extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.TimeSpentAbroad)

  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    else Ok(views.html.s5_time_spent_abroad.g6_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size == 4) Redirect(controllers.s6_education.routes.G1YourCourseDetails.present())
    else Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
  }
}