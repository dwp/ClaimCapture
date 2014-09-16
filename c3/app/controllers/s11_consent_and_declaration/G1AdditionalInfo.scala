package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.optional
import models.view.CachedClaim
import models.domain._
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import controllers.Mappings._
import models.yesNo.YesNoWithText
import controllers.Mappings
import models.view.CachedClaim.ClaimResult

object G1AdditionalInfo extends Controller with CachedClaim with Navigable {

  val anythingElseMapping =
    "anythingElse" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(maxLength = Mappings.twoThousand))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    anythingElseMapping,
    "welshCommunication" -> nonEmptyText
  )(AdditionalInfo.apply)(AdditionalInfo.unapply))

  def present:Action[AnyContent] = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(AdditionalInfo) { implicit claim => Ok(views.html.s11_consent_and_declaration.g1_additionalInfo(form.fill(AdditionalInfo))) }
  }

  def submit:Action[AnyContent] = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g1_additionalInfo(formWithErrors)),
      additionalInfo => claim.update(additionalInfo) -> Redirect(routes.G3Disclaimer.present()))
  }
}
