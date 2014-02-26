package controllers.s10_pay_details

import play.api.mvc.{AnyContent, Request, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.Claim
import play.api.i18n.Lang

object PayDetails extends Controller with CachedClaim with Navigable {
  val redirectPath = Redirect(controllers.preview.routes.Preview.present)

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult = {
    if (models.domain.PayDetails.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult =
    claim -> redirectPath
}