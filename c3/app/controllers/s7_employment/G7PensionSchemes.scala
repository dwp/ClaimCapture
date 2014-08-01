package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.PensionSchemes
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.Mappings._
import play.api.data.FormError

object G7PensionSchemes extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "payOccupationalPensionScheme" -> nonEmptyText.verifying(validYesNo),
    "howMuchPension" -> optional(nonEmptyText verifying validCurrency5Required),
    "howOftenPension" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "payPersonalPensionScheme" -> nonEmptyText.verifying(validYesNo),
    "howMuchPersonal" -> optional(nonEmptyText verifying validCurrency5Required),
    "howOftenPersonal" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly)
  )(PensionSchemes.apply)(PensionSchemes.unapply)
    .verifying("howMuchPension", PensionSchemes.validateHowMuchPension _)
    .verifying("howOftenPension.required", PensionSchemes.validateHowOftenPension _)
    .verifying("howMuchPersonal", PensionSchemes.validateHowMuchPersonal _)
    .verifying("howOftenPersonal.required", PensionSchemes.validateHowOftenPersonal _)
  )

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(PensionSchemes) { implicit claim => Ok(views.html.s7_employment.g7_pensionSchemes(form.fillWithJobID(PensionSchemes, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchPension", "decimal.invalid", FormError("howMuchPension", "decimal.invalid", Seq(labelForEmployment(claim, lang, "howMuchPension", jobID))))
          .replaceError("howMuchPersonal", "decimal.invalid", FormError("howMuchPersonal", "decimal.invalid", Seq(labelForEmployment(claim, lang, "howMuchPersonal", jobID))))
          .replaceError("payOccupationalPensionScheme", "error.required", FormError("payOccupationalPensionScheme", "error.required", Seq(labelForEmployment(claim, lang, "payOccupationalPensionScheme", jobID))))
          .replaceError("payPersonalPensionScheme", "error.required", FormError("payPersonalPensionScheme", "error.required", Seq(labelForEmployment(claim, lang, "payPersonalPensionScheme", jobID))))
          .replaceError("", "howMuchPension", FormError("howMuchPension", "error.required", Seq(labelForEmployment(claim, lang, "howMuchPension", jobID))))
          .replaceError("", "howMuchPersonal", FormError("howMuchPersonal", "error.required", Seq(labelForEmployment(claim, lang, "howMuchPersonal", jobID))))
          .replaceError("", "howOftenPension.required", FormError("howOftenPension", "error.required", Seq(labelForEmployment(claim, lang, "howOftenPension", jobID))))
          .replaceError("", "howOftenPersonal.required", FormError("howOftenPersonal", "error.required", Seq(labelForEmployment(claim, lang, "howOftenPersonal", jobID))))
          .replaceError("howOftenPension.frequency.other","error.maxLength",FormError("howOftenPension","error.maxLength"))
          .replaceError("howOftenPersonal.frequency.other","error.maxLength",FormError("howOftenPersonal","error.maxLength"))
        BadRequest(views.html.s7_employment.g7_pensionSchemes(formWithErrorsUpdate))
      },
      schemes => claim.update(jobs.update(schemes)) -> Redirect(routes.G8AboutExpenses.present(jobID)))
  }
}