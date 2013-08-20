package controllers.s9_other_money

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import controllers.Routing

object OtherMoney extends Controller with OtherMoneyRouting with CachedClaim {
  def completed = claiming { implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutOtherMoney.present())
    else Ok(views.html.s9_other_money.g8_completed(completedQuestionGroups.map(qg => qg -> route(qg))))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.OtherMoney).firstPage)
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.OtherMoney)
  }
}

trait OtherMoneyRouting extends Routing {
  override def route(qgi: QuestionGroup.Identifier) = qgi match {
    case AboutOtherMoney => routes.G1AboutOtherMoney.present()
    case StatutorySickPay => routes.G5StatutorySickPay.present()
    case OtherStatutoryPay => routes.G6OtherStatutoryPay.present()
    case OtherEEAStateOrSwitzerland => routes.G7OtherEEAStateOrSwitzerland.present()
  }
}