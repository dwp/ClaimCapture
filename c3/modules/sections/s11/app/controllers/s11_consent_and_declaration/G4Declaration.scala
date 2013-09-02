package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable

object G4Declaration extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "confirm" -> nonEmptyText,
    "someoneElse" -> optional(text)
  )(Declaration.apply)(Declaration.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(Declaration) { implicit claim => Ok(views.html.s11_consent_and_declaration.g4_declaration(form.fill(Declaration))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g4_declaration(formWithErrors)),
      declaration => claim.update(declaration) -> Redirect(routes.G5Submit.present()))
  }
}