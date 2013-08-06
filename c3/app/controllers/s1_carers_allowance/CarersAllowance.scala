package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._

object CarersAllowance extends Controller with CachedClaim {
  def completedQuestionGroups(questionGroupIdentifier: QuestionGroup.Identifier)(implicit claim: Claim, request: Request[AnyContent]): List[(QuestionGroup, Call)] = {
    claim.completedQuestionGroups(questionGroupIdentifier).map(qg => qg -> route(qg))
  }
  
  def approve = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CarersAllowance)
    val approved = claim.completedQuestionGroups(models.domain.CarersAllowance).forall(_.asInstanceOf[BooleanConfirmation].answer) && completedQuestionGroups.length == 4

    Ok(views.html.s1_carers_allowance.g6_approve(approved, claim.completedQuestionGroups(models.domain.CarersAllowance).map(qg => qg -> route(qg))))
  }

  def approveSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G5CarersResponse.present())
  }

  def claiming(qi: QuestionGroup.Identifier, claim: Claim) = claim.questionGroup(qi) match {
    case Some(b: BooleanConfirmation) => b.answer
    case _ => false
  }

  private def route(qg: QuestionGroup) = qg.identifier match {
    case BenefitsMandatory => routes.G1BenefitsMandatory.present()
    case HoursMandatory => routes.G2HoursMandatory.present()
    case Over16Mandatory => routes.G3Over16Mandatory.present()
    case LivesInGBMandatory => routes.G4LivesInGBMandatory.present()
  }
}