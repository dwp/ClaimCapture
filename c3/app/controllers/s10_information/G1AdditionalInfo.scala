package controllers.s10_information

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import controllers.Mappings._
import models.yesNo.YesNoWithText

object G1AdditionalInfo extends Controller with CachedClaim with Navigable {

  val anythingElseMapping =
    "anythingElse" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(maxLength = 2000))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    anythingElseMapping,
    "welshCommunication" -> nonEmptyText
  )(AdditionalInfo.apply)(AdditionalInfo.unapply))

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(AdditionalInfo) { implicit claim => Ok(views.html.s10_information.g1_additionalInfo(form.fill(AdditionalInfo))) }
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s10_information.g1_additionalInfo(formWithErrors)),
      additionalInfo => claim.update(additionalInfo) -> Redirect(controllers.s12_consent_and_declaration.routes.G2Disclaimer.present()))
  }
}