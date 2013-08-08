package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import controllers.Routing

object CarersAllowance extends Controller with CarersAllowanceRouting with CachedClaim {
  def approve = claiming { implicit claim => implicit request =>
    val approved = completedQuestionGroups.forall(_.asInstanceOf[BooleanConfirmation].answer) && completedQuestionGroups.length == 4

    Ok(views.html.s1_carers_allowance.g6_approve(approved, completedQuestionGroups.map(qg => qg -> route(qg))))
  }

  def approveSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G5CarersResponse.present())
  }

  def claiming(qi: QuestionGroup.Identifier, claim: Claim) = claim.questionGroup(qi) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.CarersAllowance)
  }
}

trait CarersAllowanceRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case Benefits => routes.G1Benefits.present()
    case Hours => routes.G2Hours.present()
    case Over16 => routes.G3Over16.present()
    case LivesInGB => routes.G4LivesInGB.present()
  }
}