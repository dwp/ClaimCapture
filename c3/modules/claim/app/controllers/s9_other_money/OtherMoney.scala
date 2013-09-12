package controllers.s9_other_money

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object OtherMoney extends Controller with CachedClaim with Navigable {
  def completed = executeOnForm {implicit claim => implicit request =>
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutOtherMoney.present())
    else track(models.domain.OtherMoney) { implicit claim => Ok(views.html.s9_other_money.g8_completed())}
  }

  def completedSubmit = executeOnForm {implicit claim => implicit request =>
    Redirect("/pay-details/how-we-pay-you")
  }

  private def completedQuestionGroups(implicit claim: DigitalForm): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.OtherMoney)
  }
}