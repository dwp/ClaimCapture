package controllers.s1_carers_allowance

import play.api.mvc._
import models.view._
import models.domain._
import controllers.Routing

object CarersAllowance extends Controller with CachedClaim with Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case BenefitsMandatory => routes.G1BenefitsMandatory.present()
    case HoursMandatory => routes.G2HoursMandatory.present()
    case Over16Mandatory => routes.G3Over16Mandatory.present()
    case LivesInGBMandatory => routes.G4LivesInGBMandatory.present()
  }

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