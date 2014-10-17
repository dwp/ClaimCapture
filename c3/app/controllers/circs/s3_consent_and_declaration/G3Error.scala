package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, CachedClaim, Navigable}

object G3Error extends Controller with CachedChangeOfCircs with Navigable {
  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(models.domain.Error) { implicit circs => Ok(views.html.circs.s3_consent_and_declaration.g3_error()) }
  }
}