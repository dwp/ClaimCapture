package controllers.circs.s3_consent_and_declaration

import play.api.mvc.Controller
import models.view.{Navigable, CachedCircs}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.{CircumstancesDeclaration, CircumstancesOtherInfo}
import controllers.circs.s1_identification.routes

object G1Declaration extends Controller with CachedCircs with Navigable {
  val form = Form(mapping(
    "obtainInfoAgreement" -> nonEmptyText,
    "obtainInfoWhy" -> optional(nonEmptyText(maxLength = 2000)),
    "confirm" -> nonEmptyText
  )(CircumstancesDeclaration.apply)(CircumstancesDeclaration.unapply)
    .verifying("obtainInfoWhy", CircumstancesDeclaration.validateWhy _))

  def present = executeOnForm { implicit claim => implicit request =>
    track(CircumstancesOtherInfo) { implicit claim => Ok(views.html.circs.s3_consent_and_declaration.g1_declaration(form.fill(CircumstancesDeclaration))) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val f = formWithErrors.replaceError("", "obtainInfoWhy", FormError("obtainInfoWhy", "error.required"))
          BadRequest(views.html.circs.s3_consent_and_declaration.g1_declaration(f))
        },
        f => claim.update(f) -> {
          claim.isBot(f) match {
            case true => NotFound(views.html.errors.onHandlerNotFound(request)) // Send bot to 404 page.
            case false => Redirect("/circs-submit")
          }
        }
      )
  }
}
