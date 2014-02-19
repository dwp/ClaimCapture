package controllers.s8_self_employment

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object SelfEmployment extends Controller with CachedClaim with Navigable {
  def completed = claiming {implicit claim => implicit request => implicit lang =>
    redirect
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    redirect
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.SelfEmployment.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect("/other-money/about-other-money")
}