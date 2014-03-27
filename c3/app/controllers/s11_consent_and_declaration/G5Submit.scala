package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable
import app.ConfigProperties._
import services.async.AsyncActors

object G5Submit extends Controller with CachedClaim with Navigable {
  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(models.domain.Submit) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit()) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Redirect(routes.G7Submitting.present())
  }
}