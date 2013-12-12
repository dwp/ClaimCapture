package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object TimeSpentAbroad extends Controller with CachedClaim with Navigable {
  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request => implicit lang =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    else track(TimeSpentAbroad) { implicit claim => Ok(views.html.s5_time_spent_abroad.g6_completed())}
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    if (completedQuestionGroups.distinct.size == 4) Redirect("/education/your-course-details")
    else Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.TimeSpentAbroad)
  }
}