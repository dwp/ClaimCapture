package controllers.s9_other_money

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object OtherMoney extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request => implicit lang =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutOtherMoney.present())
    else track(models.domain.OtherMoney) { implicit claim => Ok(views.html.s9_other_money.g8_completed())}
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    Redirect("/pay-details/how-we-pay-you")
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.OtherMoney)
  }
}