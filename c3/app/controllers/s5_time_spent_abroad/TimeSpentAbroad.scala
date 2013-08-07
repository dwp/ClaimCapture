package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import models.domain._

object TimeSpentAbroad extends Controller with TimeSpentAbroadRouting with CachedClaim {
  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    else Ok(views.html.s5_time_spent_abroad.g6_completed(completedQuestionGroups.filterNot(_.isInstanceOf[Trips]).map(qg => qg -> route(qg))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size == 4) Redirect(controllers.s6_education.routes.G1YourCourseDetails.present())
    else Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.TimeSpentAbroad)
  }
}

trait TimeSpentAbroadRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case NormalResidenceAndCurrentLocation => routes.G1NormalResidenceAndCurrentLocation.present()
    case AbroadForMoreThan4Weeks => routes.G2AbroadForMoreThan4Weeks.present()
    case AbroadForMoreThan52Weeks => routes.G3AbroadForMoreThan52Weeks.present()
  }
}