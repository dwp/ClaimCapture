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
    "howMuchPension" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOftenPension" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "payPersonalPensionScheme" -> nonEmptyText.verifying(validYesNo),
    "howMuchPersonal" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOftenPersonal" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly)
  )(PensionSchemes.apply)(PensionSchemes.unapply)
    .verifying("howMuchPension", PensionSchemes.validateHowMuchPension _)
    .verifying("howOftenPension.required", PensionSchemes.validateHowOftenPension _)
    .verifying("howMuchPersonal", PensionSchemes.validateHowMuchPersonal _)
    .verifying("howOftenPersonal.required", PensionSchemes.validateHowOftenPersonal _)
  )

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(PensionSchemes) { implicit claim => Ok(views.html.s7_employment.g7_pensionSchemes(form.fillWithJobID(PensionSchemes, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresent = pastPresentLabelForEmployment(claim, didYou, doYou , jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchPension", "decimal.invalid", FormError("howMuchPension", "decimal.invalid", Seq(pastPresent.toLowerCase)))
          .replaceError("howMuchPersonal", "decimal.invalid", FormError("howMuchPersonal", "decimal.invalid", Seq(pastPresent.toLowerCase)))
          .replaceError("payOccupationalPensionScheme", "error.required", FormError("payOccupationalPensionScheme", "error.required", Seq(pastPresent)))
          .replaceError("payPersonalPensionScheme", "error.required", FormError("payPersonalPensionScheme", "error.required", Seq(pastPresent)))
          .replaceError("", "howMuchPension", FormError("howMuchPension", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("", "howMuchPersonal", FormError("howMuchPersonal", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("", "howOftenPension.required", FormError("howOftenPension", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("", "howOftenPersonal.required", FormError("howOftenPersonal", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("howOftenPension.frequency.other","error.maxLength",FormError("howOftenPension","error.maxLength"))
          .replaceError("howOftenPersonal.frequency.other","error.maxLength",FormError("howOftenPersonal","error.maxLength"))
        BadRequest(views.html.s7_employment.g7_pensionSchemes(formWithErrorsUpdate))
      },
      schemes => claim.update(jobs.update(schemes)) -> Redirect(routes.G8AboutExpenses.present(jobID)))
  }
}