package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}

object G7Submitting extends Controller with CachedClaim with Navigable {
  def present = claiming { implicit claim => implicit request =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g7_submitting()) }
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("/submit")
  }
}