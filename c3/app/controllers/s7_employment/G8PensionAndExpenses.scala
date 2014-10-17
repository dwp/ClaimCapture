package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.PensionAndExpenses
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.Mappings._
import play.api.data.FormError
import controllers.CarersForms._
import models.yesNo.YesNoWithText

object G8PensionAndExpenses extends Controller with CachedClaim with Navigable {

  val payPensionScheme =
    "payPensionScheme" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersTextWithPound(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payPensionScheme.text.required", YesNoWithText.validateOnYes _)

  val haveExpensesForJob =
    "haveExpensesForJob" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersTextWithPound(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("haveExpensesForJob.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    payPensionScheme,
    haveExpensesForJob
  )(PensionAndExpenses.apply)(PensionAndExpenses.unapply))


  def present(jobID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(PensionAndExpenses) { implicit claim => Ok(views.html.s7_employment.g8_pensionAndExpenses(form.fillWithJobID(PensionAndExpenses, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payPensionScheme","payPensionScheme.text.required",FormError("payPensionScheme.text","error.required", Seq(labelForEmployment(claim, lang, "payPensionScheme.text", jobID))))
          .replaceError("payPensionScheme","payPensionScheme.text..maxLength",FormError("payPensionScheme.text","error.maxLength", Seq(labelForEmployment(claim, lang, "payPensionScheme.text", jobID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.required",FormError("haveExpensesForJob.text","error.required", Seq(labelForEmployment(claim, lang, "haveExpensesForJob.text", jobID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text..maxLength",FormError("haveExpensesForJob.text","error.maxLength", Seq(labelForEmployment(claim, lang, "haveExpensesForJob.text", jobID))))

          BadRequest(views.html.s7_employment.g8_pensionAndExpenses(formWithErrorsUpdate))
      },
      aboutExpenses => claim.update(jobs.update(aboutExpenses).completeJob(jobID)) -> Redirect(routes.G2BeenEmployed.present()))

  }
}