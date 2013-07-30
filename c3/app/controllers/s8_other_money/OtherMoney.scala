package controllers.s8_other_money

import play.api.mvc._
import models.view.CachedClaim
import models.domain.Claim

object OtherMoney extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.OtherMoney)

  def completed = claiming {
    implicit claim => implicit request =>
      if (completedQuestionGroups.isEmpty) Redirect(routes.G1AboutOtherMoney.present())
      else Ok(views.html.s8_other_money.g8_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming {
    implicit claim => implicit request =>
      Redirect(claim.nextSection(models.domain.OtherMoney).firstPage)
  }
}