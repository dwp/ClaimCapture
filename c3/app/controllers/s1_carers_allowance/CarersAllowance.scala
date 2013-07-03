package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import scala.collection.immutable.ListMap

object CarersAllowance extends Controller with CachedClaim {
  val route: ListMap[String, Call] = ListMap(
    G1Benefits,
    G2Hours,
    G3Over16,
    G4LivesInGB)

  def approve = claiming { implicit claim => implicit request =>
    val sectionId = models.domain.CarersAllowance.id
    val completedQuestionGroups = claim.completedQuestionGroups(sectionId)
    val approved = claim.completedQuestionGroups(sectionId).forall(_.asInstanceOf[BooleanConfirmation].answer) && completedQuestionGroups.length == 4

    Ok(views.html.s1_carersallowance.g5_approve(approved, completedQuestionGroups))
  }

  def approveSubmit = Action {
    Redirect(controllers.s2_about_you.routes.G1YourDetails.present)
  }

  def claiming(formID: String, claim: Claim) = claim.questionGroup(formID) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }
}