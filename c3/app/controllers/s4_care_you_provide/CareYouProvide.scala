package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import controllers.Routing._
import models.domain.Claim

object CareYouProvide extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(
    G1TheirPersonalDetails,
    G2TheirContactDetails,
    G3MoreAboutThePerson,
    G4PreviousCarerPersonalDetails,
    G5PreviousCarerContactDetails,
    G6RepresentativesForThePerson,
    G7MoreAboutTheCare,
    G8OneWhoPaysPersonalDetails,
    G9ContactDetailsOfPayingPerson,
    G10BreaksInCare)

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present)
    else Ok(views.html.s4_care_you_provide.completed(completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.distinct.size >= 6) Redirect(controllers.s5_time_spent_abroad.routes.G1NormalResidenceAndCurrentLocation.present)
    else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }
}