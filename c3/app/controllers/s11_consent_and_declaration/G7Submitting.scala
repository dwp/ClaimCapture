package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}

object G7Submitting extends Controller with CachedClaim with Navigable {
  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g7_submitting()) }
  }
}