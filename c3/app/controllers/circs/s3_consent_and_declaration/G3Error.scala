package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, CachedClaim, Navigable}

object G3Error extends Controller with CachedChangeOfCircs with Navigable {
  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(models.domain.Error) { implicit claim => Ok(views.html.circs.s3_consent_and_declaration.g3_error()) }
  }
}