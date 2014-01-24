package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import models.domain._

object YourPartner extends Controller with CachedClaim with Navigable {

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourPartner.visible) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
}