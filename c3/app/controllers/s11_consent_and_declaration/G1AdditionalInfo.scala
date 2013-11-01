package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms

object G1AdditionalInfo extends Controller with CachedClaim{
  val form = Form(
    mapping(
      "anythingElse" -> optional(CarersForms.carersText(maxLength = 2000)),
      "welshCommunication" -> nonEmptyText
    )(AdditionalInfo.apply)(AdditionalInfo.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g1_additionalInfo(form.fill(AdditionalInfo)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g1_additionalInfo(formWithErrors)),
      additionalInfo => claim.update(additionalInfo) -> Redirect(routes.G2Consent.present()))
  }
}