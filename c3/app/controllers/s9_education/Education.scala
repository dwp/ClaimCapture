package controllers.s9_education

import play.api.mvc.Controller
import models.view.CachedClaim
import scala.collection.immutable.ListMap
import play.api.mvc.Call
import controllers.Routing._
import models.domain.{BreaksInCare, Claim}

object Education extends Controller with CachedClaim {

  val route: ListMap[String, Call] = ListMap(
    G2AddressOfSchoolCollegeOrUniversity)

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.Education.id)

  def completed = claiming { implicit claim => implicit request =>
      Ok(views.html.s9_education.completed(completedQuestionGroups))
  }
  
  def completedSubmit = claiming { implicit claim => implicit request =>
    ???
  }
}