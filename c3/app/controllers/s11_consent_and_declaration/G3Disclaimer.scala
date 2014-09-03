package controllers.s11_consent_and_declaration

import controllers.CarersForms._

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
    "read" -> carersNonEmptyText
  )(Disclaimer.apply)(Disclaimer.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(Disclaimer) { implicit claim => Ok(views.html.s11_consent_and_declaration.g3_disclaimer(form.fill(Disclaimer))) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g3_disclaimer(formWithErrors)),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G4Declaration.present()))
  }
}