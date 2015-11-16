package controllers.s_employment

import play.api.Play._

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
import play.api.i18n._


object GEmploymentAdditionalInfo extends Controller with CachedClaim with Navigable with I18nSupport{
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val additionalInfo =
    "empAdditionalInfo" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 3000))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("empAdditionalInfo.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    additionalInfo
  )(EmploymentAdditionalInfo.apply)(EmploymentAdditionalInfo.unapply))

  def present = claimingWithCheck {implicit claim => implicit request => implicit lang => 
     track(EmploymentAdditionalInfo) { implicit claim => Ok(views.html.s_employment.g_employmentAdditionalInfo(form.fill(EmploymentAdditionalInfo))) }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
        .replaceError("empAdditionalInfo", "empAdditionalInfo.text.required", FormError("empAdditionalInfo.text", errorRequired))
        BadRequest(views.html.s_employment.g_employmentAdditionalInfo(formWithErrorsUpdate))
      },
      employmentAdditionalInfo => claim.update(employmentAdditionalInfo) -> Redirect(controllers.s_other_money.routes.GAboutOtherMoney.present())
    )
  } withPreview()
}
