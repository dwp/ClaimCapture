package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.LastWage
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import controllers.CarersForms._
import utils.helpers.PastPresentLabelHelper._


object G5LastWage extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "oftenGetPaid" -> (mandatoryPaymentFrequency verifying validPaymentFrequencyOnly),
    "whenGetPaid" -> optional(carersText),
    "lastPaidDate" -> dayMonthYear.verifying(validDate),
    "grossPay" -> required(nonEmptyText.verifying(validCurrencyRequired)),
    "payInclusions" -> optional(carersText(maxLength = 100)),
    "sameAmountEachTime" -> optional(text.verifying(validYesNo)),
    "employerOwesYouMoney" -> (nonEmptyText verifying validYesNo)
  )(LastWage.apply)(LastWage.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(LastWage) { implicit claim => Ok(views.html.s7_employment.g5_lastWage(form.fillWithJobID(LastWage, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val form = formWithErrors
          .replaceError("oftenGetPaid.frequency.other","error.maxLength",FormError("oftenGetPaid","error.maxLength"))
          .replaceError("oftenGetPaid.frequency","error.required",FormError("oftenGetPaid","error.required"))
          .replaceError("lastPaidDate", "error.required", FormError("lastPaidDate", "error.required", Seq(labelForEmployment(claim, lang, "lastPaidDate", jobID))))
          .replaceError("grossPay", "error.required", FormError("grossPay", "error.required", Seq(labelForEmployment(claim, lang, "grossPay", jobID))))
          .replaceError("employerOwesYouMoney", "error.required", FormError("employerOwesYouMoney", "error.required", Seq(labelForEmployment(claim, lang, "employerOwesYouMoney", jobID))))
        BadRequest(views.html.s7_employment.g5_lastWage(form))
      },
      lastWage => claim.update(jobs.update(lastWage)) -> Redirect(routes.G7PensionSchemes.present(jobID)))
  }
}