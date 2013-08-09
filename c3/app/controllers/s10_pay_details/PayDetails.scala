package controllers.s10_pay_details

import play.api.mvc.{Result, Controller}
import models.view.CachedClaim
import models.domain.{BankBuildingSocietyDetails, HowWePayYou, QuestionGroup, Claim}
import controllers.Routing

object PayDetails extends Controller with PayDetailsRouting with CachedClaim {
  def whenSectionVisible(f: => Either[Result, (Claim, Result)])(implicit claim: Claim): Either[Result, (Claim, Result)] = {
    if (claim.isSectionVisible(models.domain.PayDetails)) f
    else Redirect(claim.nextSection(models.domain.PayDetails).firstPage)
  }

  def completed = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s10_pay_details.g3_completed(completedQuestionGroups.map(qg => qg -> route(qg)))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.PayDetails).firstPage)
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.PayDetails)
  }
}

trait PayDetailsRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case HowWePayYou => routes.G1HowWePayYou.present()
    case BankBuildingSocietyDetails => routes.G2BankBuildingSocietyDetails.present()
  }
}