package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import models.domain._

object YourPartner extends Controller with CachedClaim with Navigable {
  def completed = claiming {implicit claim => implicit request => implicit lang =>
    presentConditionally {
      track(models.domain.YourPartner) { implicit claim => Ok(views.html.s3_your_partner.g5_completed()) }
    }
  }

  def completedSubmit = claiming { implicit claim => implicit request => implicit lang =>
    redirect
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourPartner.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    /* TODO Sort out hardcoding */
    claim -> Redirect("/care-you-provide/their-personal-details")
}