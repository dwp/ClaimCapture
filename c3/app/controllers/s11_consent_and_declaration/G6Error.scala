package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable

object G6Error extends Controller with CachedClaim with Navigable {
  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(models.domain.Error) { implicit claim => Ok(views.html.s11_consent_and_declaration.g6_error()) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    Redirect(routes.G5Submit.present())
  }
}