package controllers.s3_your_partner

import play.api.mvc._
import models.view._
import models.domain._

object YourPartner extends Controller with CachedClaim with Navigable {
  def completed = executeOnForm {implicit claim => implicit request =>
    presentConditionally {
      track(models.domain.YourPartner) { implicit claim => Ok(views.html.s3_your_partner.g5_completed()) }
    }
  }

  def completedSubmit = executeOnForm {implicit claim => implicit request =>
    redirect
  }

  def presentConditionally(c: => FormResult)(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    if (models.domain.YourPartner.visible) c
    else redirect
  }

  def redirect(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult =
    /* TODO Sort out hardcoding */
    claim -> Redirect("/care-you-provide/their-personal-details")
}