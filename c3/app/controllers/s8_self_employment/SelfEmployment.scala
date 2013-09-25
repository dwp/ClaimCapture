package controllers.s8_self_employment

import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable

object SelfEmployment extends Controller with CachedClaim with Navigable {
  def completed = executeOnForm {implicit claim => implicit request =>
    presentConditionally {
      track(models.domain.SelfEmployment) { implicit claim => Ok(views.html.s8_self_employment.g9_completed()) }
    }
  }

  def completedSubmit = executeOnForm {implicit claim => implicit request =>
    redirect
  }

  def presentConditionally(c: => FormResult)(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    if (models.domain.SelfEmployment.visible) c
    else redirect
  }

  def redirect(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult =
    claim -> Redirect("/other-money/about-other-money")
}