package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._

object G4Declaration extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "confirm" -> carersNonEmptyText,
    "nameOrOrganisation" -> optional(carersNonEmptyText(maxLength = 60)),
    "someoneElse" -> optional(carersText)
  )(Declaration.apply)(Declaration.unapply)
    .verifying("nameOrOrganisation",Declaration.validateNameOrOrganisation _))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(Declaration) { implicit claim => Ok(views.html.s11_consent_and_declaration.g4_declaration(form.fill(Declaration))) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("","nameOrOrganisation", FormError("nameOrOrganisation", "error.required"))
        BadRequest(views.html.s11_consent_and_declaration.g4_declaration(updatedFormWithErrors))
      },
      declaration => claim.update(declaration) -> Redirect(routes.G5Submit.present()))
  }
}