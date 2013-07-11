package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import models.domain.{Trips, Claim}

object TimeSpentAbroad extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(
    G1NormalResidenceAndCurrentLocation,
    G2AbroadForMoreThan4Weeks)

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

  def trips(implicit claim: Claim) = claim.questionGroup(Trips) match {
    case Some(ts: Trips) => ts
    case _ => Trips()
  }

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1NormalResidenceAndCurrentLocation.present())
    //else Ok(views.html.s5_time_spent_abroad.completed(completedQuestionGroups)) TODO
    else Ok("")
  }

  /*def submit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size >= 6) Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present())
    else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }*/
}