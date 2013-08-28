package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import controllers.Routing

object CarersAllowance extends Controller with CachedClaim with Navigable {
  def approve = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CarersAllowance)
    val approved = completedQuestionGroups.length == 4 &&
      completedQuestionGroups.forall(_.asInstanceOf[BooleanConfirmation].answer)

    track(LivesInGB) { implicit claim => Ok(views.html.s1_carers_allowance.g6_approve(approved))}
  }

  def approveSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G5CarersResponse.present())
  }

  def claiming(qi: QuestionGroup.Identifier, claim: Claim) = claim.questionGroup(qi) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }
}