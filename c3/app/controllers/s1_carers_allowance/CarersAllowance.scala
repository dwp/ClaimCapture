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
    
  val progressBar = ProgressBar(models.domain.CarersAllowance.id)

  def approve = claiming { implicit claim => implicit request =>
    val sectionId = models.domain.CarersAllowance.id
    val completedQuestionGroups = claim.completedQuestionGroups(sectionId)
    val approved = claim.completedQuestionGroups(sectionId).forall(_.asInstanceOf[BooleanConfirmation].answer) && completedQuestionGroups.length == 4

    Ok(views.html.s1_carers_allowance.g5_approve(approved, completedQuestionGroups, completedSections = progressBar.completedSections, activeSection = progressBar.activeSection, futureSections = progressBar.futureSections))
  }

  def approveSubmit = Action {
    Redirect(controllers.s2_about_you.routes.G1YourDetails.present())
  }

  def claiming(questionGroup: QuestionGroup, claim: Claim) = claim.questionGroup(questionGroup) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }
}