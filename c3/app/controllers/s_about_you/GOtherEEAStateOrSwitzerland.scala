package controllers.s_about_you

import controllers.CarersForms._
import models.domain.OtherEEAStateOrSwitzerland
import models.view.{CachedClaim, Navigable}
import models.yesNo.{YesNoWith1MandatoryFieldOnYes, YesNoWith2MandatoryFieldsOnYes}
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n.{MMessages => Messages}
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import utils.helpers.YesNoHelpers

import scala.language.reflectiveCalls

object GOtherEEAStateOrSwitzerland extends Controller with CachedClaim with Navigable {

  def yesNo1Field = mapping(
    "answer" -> nonEmptyText(),
    "field" -> optional(carersNonEmptyText(maxLength = 3000))
  )(YesNoWith1MandatoryFieldOnYes.apply)(YesNoWith1MandatoryFieldOnYes.unapply)

  val form = Form(mapping(
    "eeaGuardQuestion" -> mapping(
      "answer" -> nonEmptyText(),
      "benefitsFromEEADetails" -> optional(yesNo1Field),
      "workingForEEADetails" -> optional(yesNo1Field)
    )(YesNoWith2MandatoryFieldsOnYes.apply)(YesNoWith2MandatoryFieldsOnYes.unapply)
      .verifying(error = "error.benefitsFromEEADetails.required", constraint = YesNoWith2MandatoryFieldsOnYes.validateField1OnYes[YesNoWith1MandatoryFieldOnYes[String]] _)
      .verifying(error = "error.workingForEEADetails.required", constraint = YesNoWith2MandatoryFieldsOnYes.validateField2OnYes[YesNoWith1MandatoryFieldOnYes[String]] _)
      .verifying(OtherEEAStateOrSwitzerland.requiredBenefitsFromEEADetails)
      .verifying(OtherEEAStateOrSwitzerland.requiredWorkingForEEADetails)

  )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply)
  )

  def present = claimingWithCheck { implicit claim => implicit request => lang =>
    track(OtherEEAStateOrSwitzerland) { implicit claim => Ok(views.html.s_about_you.g_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland))(lang)) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("eeaGuardQuestion", "error.benefitsFromEEADetails.required", FormError("eeaGuardQuestion.benefitsFromEEADetails.answer", "error.benefitsFromEEADetails.required"))
          .replaceError("eeaGuardQuestion", "error.workingForEEADetails.required", FormError("eeaGuardQuestion.workingForEEADetails.answer", "error.workingForEEADetails.required"))
          .replaceError("eeaGuardQuestion", "benefitsfromeeadetails.required", FormError("eeaGuardQuestion.benefitsFromEEADetails.field", "error.benefitsFromEEADetails.required"))
          .replaceError("eeaGuardQuestion", "workingForEEADetails.required", FormError("eeaGuardQuestion.workingForEEADetails.field", "error.workingForEEADetails.required"))
        BadRequest(views.html.s_about_you.g_otherEEAStateOrSwitzerland(formWithErrorsUpdate)(lang))
      },
      benefitsFromEEA => claim.update(benefitsFromEEA) -> Redirect(controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present())
    )
  } withPreview()
}