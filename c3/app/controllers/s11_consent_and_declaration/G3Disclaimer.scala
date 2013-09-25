package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable

object G3Disclaimer extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "read" -> nonEmptyText
  )(Disclaimer.apply)(Disclaimer.unapply))

  def present = executeOnForm { implicit claim => implicit request =>
    track(Disclaimer) { implicit claim => Ok(views.html.s11_consent_and_declaration.g3_disclaimer(form.fill(Disclaimer))) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g3_disclaimer(formWithErrors)),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G4Declaration.present()))
  }
}