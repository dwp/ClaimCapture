package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import models.domain.{Trips, Claim}

object TimeSpentAbroad extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(
    G1NormalResidenceAndCurrentLocation,
    G2AbroadForMoreThan4Weeks,
    G3AbroadForMoreThan52Weeks,
    G5otherEEAStateOrSwitzerland)

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.TimeSpentAbroad.id)

  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    else Ok(views.html.s5_time_spent_abroad.completed(completedQuestionGroups))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size == 5) Redirect(controllers.s6_pay_details.routes.G1HowWePayYou.present())
    else Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
  }
}