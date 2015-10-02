package controllers.s_employment

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import models.view.{Navigable, CachedClaim}
import models.domain.EmploymentAdditionalInfo
import models.yesNo.YesNoWithText


object GEmploymentAdditionalInfo extends Controller with CachedClaim with Navigable{

  val additionalInfo =
    "empAdditionalInfo" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 3000))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("empAdditionalInfo.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    additionalInfo
  )(EmploymentAdditionalInfo.apply)(EmploymentAdditionalInfo.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
     track(EmploymentAdditionalInfo) { implicit claim => Ok(views.html.s_employment.g_employmentAdditionalInfo(form.fill(EmploymentAdditionalInfo))(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
        .replaceError("empAdditionalInfo", "empAdditionalInfo.text.required", FormError("empAdditionalInfo.text", errorRequired))
        BadRequest(views.html.s_employment.g_employmentAdditionalInfo(formWithErrorsUpdate)(lang))
      },
      employmentAdditionalInfo => claim.update(employmentAdditionalInfo) -> Redirect(controllers.s_other_money.routes.GAboutOtherMoney.present())
    )
  } withPreview()
}
