package controllers.s8_self_employment

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object SelfEmployment extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    presentConditionally {
      track(models.domain.SelfEmployment) { implicit claim => Ok(views.html.s8_self_employment.g9_completed()) }
    }
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    redirect
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (claim.isSectionVisible(models.domain.SelfEmployment)) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s9_other_money.routes.G1AboutOtherMoney.present())
}