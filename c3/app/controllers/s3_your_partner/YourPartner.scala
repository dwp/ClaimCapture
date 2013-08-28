package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import models.domain._

object YourPartner extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    presentConditionally(present)
  }

  def present(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(models.domain.YourPartner) { implicit claim => Ok(views.html.s3_your_partner.g5_completed()) }
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.YourPartner).firstPage)
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (claim.isSectionVisible(models.domain.YourPartner)) c
    else redirect
  }

  def redirect(implicit claim: Claim, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
}