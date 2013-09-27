package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable

object G5Submit extends Controller with CachedClaim with Navigable {
  def present = claiming { implicit claim => implicit request =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit()) }
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("/consent-and-declaration/submitting")
  }
}