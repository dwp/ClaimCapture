package controllers.s8_other_money

import play.api.mvc._
import models.view.CachedClaim
import models.domain.Claim
import play.api.templates.Html
import views.html.s8_other_money.g7_completed

object OtherMoney extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.OtherMoney)

  def completed = claiming {
    implicit claim => implicit request =>
      if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutOtherMoney.present())
      else Ok(views.html.s8_other_money.g7_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming {
    implicit claim => implicit request =>
      Redirect(controllers.s9_pay_details.routes.G1HowWePayYou.present())
  }
}