package controllers.s12_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable

object G4Error extends Controller with CachedClaim with Navigable {
  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(models.domain.Error) { implicit claim => Ok(views.html.s12_consent_and_declaration.g4_error()) }
  }
}