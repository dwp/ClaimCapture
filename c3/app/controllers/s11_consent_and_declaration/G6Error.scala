package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim

object G6Error extends Controller with CachedClaim{
  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g6_error())
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(routes.G5Submit.present())
  }
}