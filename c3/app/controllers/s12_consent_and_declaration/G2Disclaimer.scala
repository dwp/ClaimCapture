package controllers.s12_consent_and_declaration

import controllers.CarersForms._

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable

object G2Disclaimer extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "read" -> carersNonEmptyText
  )(Disclaimer.apply)(Disclaimer.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(Disclaimer) { implicit claim => Ok(views.html.s12_consent_and_declaration.g2_disclaimer(form.fill(Disclaimer))) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s12_consent_and_declaration.g2_disclaimer(formWithErrors)),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G3Declaration.present()))
  }
}