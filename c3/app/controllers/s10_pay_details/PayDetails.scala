package controllers.s10_pay_details

import play.api.mvc.{AnyContent, Request, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.DigitalForm

object PayDetails extends Controller with CachedClaim with Navigable {
  def completed = executeOnForm {implicit claim => implicit request =>
    presentConditionally {
      track(models.domain.PayDetails) { implicit claim => Ok(views.html.s10_pay_details.g3_completed()) }
    }
  }

  def completedSubmit = executeOnForm { implicit claim => implicit request =>
    redirect
  }

  def presentConditionally(c: => FormResult)(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    if (models.domain.PayDetails.visible) c
    else redirect
  }

  def redirect(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult =
    claim -> Redirect("/consent-and-declaration/additional-info")
}