package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.{CircumstancesDeclaration, CircumstancesOtherInfo}

object G1Declaration extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "obtainInfoAgreement" -> nonEmptyText,
    "obtainInfoWhy" -> optional(nonEmptyText(maxLength = 2000)),
    "confirm" -> nonEmptyText
  )(CircumstancesDeclaration.apply)(CircumstancesDeclaration.unapply)
    .verifying("obtainInfoWhy", CircumstancesDeclaration.validateWhy _))

  def present = claiming { implicit claim => implicit request =>
    track(CircumstancesOtherInfo) {
      implicit claim => Ok(views.html.circs.s3_consent_and_declaration.g1_declaration(form.fill(CircumstancesDeclaration)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "obtainInfoWhy", FormError("obtainInfoWhy", "error.required"))
        BadRequest(views.html.circs.s3_consent_and_declaration.g1_declaration(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect("/circs-submit")
    )
  }
}
