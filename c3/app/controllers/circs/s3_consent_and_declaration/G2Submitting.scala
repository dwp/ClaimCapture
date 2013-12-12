package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import models.domain.{CircumstancesDeclaration, CircumstancesOtherInfo}
import play.api.data.FormError

object G2Submitting extends Controller with CachedChangeOfCircs with Navigable {

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesOtherInfo) {
      implicit circs => Ok(views.html.circs.s3_consent_and_declaration.g2_submitting())
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    Redirect("/circs-submit")
  }

}
