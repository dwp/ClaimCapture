package controllers.s_information

import play.api.Play._

import language.reflectiveCalls
import play.api.mvc.{AnyContent, Request, Controller}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.YesNoWithText
import app.ConfigProperties._
import play.api.i18n._

object GAdditionalInfo extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val anythingElseMapping =
    "anythingElse" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(maxLength = AdditionalInfo.textMaxLength))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    anythingElseMapping,
    "welshCommunication" -> nonEmptyText
  )(AdditionalInfo.apply)(AdditionalInfo.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(AdditionalInfo) { implicit claim => Ok(views.html.s_information.g_additionalInfo(form.fill(AdditionalInfo))) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("anythingElse","text.required",FormError("anythingElse.text",errorRequired))
        BadRequest(views.html.s_information.g_additionalInfo(updatedFormWithErrors))
      },
      additionalInfo => claim.update(additionalInfo) -> redirect(claim))
  }

  private def redirect(claim: Claim) = {
    if (getProperty("preview.enabled",default = false)) {
      Redirect(controllers.preview.routes.Preview.present().url + getReturnToSummaryValue(claim))
    } else {
      Redirect(controllers.s_consent_and_declaration.routes.GDeclaration.present())
    }
  }
}
