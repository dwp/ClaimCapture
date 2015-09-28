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
    "guardQuestion" -> (mapping(
      "answer" -> nonEmptyText(),
      "field1" -> optional(yesNo1Field),
      "field2" -> optional(yesNo1Field)
    )(YesNoWith2MandatoryFieldsOnYes.apply)(YesNoWith2MandatoryFieldsOnYes.unapply)
      .verifying(error = "error.benefitsFromEEADetails.notFilled", constraint = (YesNoWith2MandatoryFieldsOnYes.validateField1OnYes[YesNoWith1MandatoryFieldOnYes[String]] _))
      .verifying(error = "error.workingForEEADetails.required", constraint = (YesNoWith2MandatoryFieldsOnYes.validateField2OnYes[YesNoWith1MandatoryFieldOnYes[String]] _))
      )
  )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply)
  )

  def present = claimingWithCheck { implicit claim => implicit request => lang =>
    track(OtherEEAStateOrSwitzerland) { implicit claim => Ok(views.html.s_about_you.g_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland))(lang)) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("guardQuestion", "error.benefitsFromEEADetails.notFilled", FormError("guardQuestion.field1.answer", "error.benefitsFromEEADetails.notFilled"))
          .replaceError("guardQuestion", "error.workingForEEADetails.required", FormError("guardQuestion.field2.answer", "error.workingForEEADetails.required"))
        BadRequest(views.html.s_about_you.g_otherEEAStateOrSwitzerland(formWithErrorsUpdate)(lang))
      },
      benefitsFromEEA => claim.update(benefitsFromEEA) -> Redirect(controllers.s_your_partner.routes.GYourPartnerPersonalDetails.present())
    )
  } withPreview()
}