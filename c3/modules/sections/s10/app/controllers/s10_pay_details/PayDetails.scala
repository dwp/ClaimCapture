package controllers.s10_pay_details

import play.api.mvc.{AnyContent, Request, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.Claim

object PayDetails extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    presentConditionally {
      track(models.domain.PayDetails) { implicit claim => Ok(views.html.s10_pay_details.g3_completed()) }
    }
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    redirect
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.PayDetails.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/consent-and-declaration/additional-info")
}