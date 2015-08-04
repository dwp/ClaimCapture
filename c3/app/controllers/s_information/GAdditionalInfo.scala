package controllers.s_information

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.YesNoWithText
import app.ConfigProperties._

object GAdditionalInfo extends Controller with CachedClaim with Navigable {

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
    track(AdditionalInfo) { implicit claim => Ok(views.html.s_information.g_additionalInfo(form.fill(AdditionalInfo))(lang)) }
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s_information.g_additionalInfo(formWithErrors)(lang)),
      additionalInfo => claim.update(additionalInfo) -> redirect())
  }

  private def redirect() = {
    if (getProperty("preview.enabled",default = false)){
      Redirect(controllers.preview.routes.Preview.present())
    }else{
      Redirect(controllers.s_consent_and_declaration.routes.GDeclaration.present())
    }
  }
}