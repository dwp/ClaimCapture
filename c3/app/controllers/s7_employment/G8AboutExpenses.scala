package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.AboutExpenses
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.Mappings._
import play.api.data.FormError
import controllers.CarersForms._

object G8AboutExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "haveExpensesForJob" -> nonEmptyText.verifying(validYesNo),
    "jobTitle" -> optional(carersNonEmptyText),
    "whatExpensesForJob" -> optional(nonEmptyText),
    "payAnyoneToLookAfterChildren" -> nonEmptyText.verifying(validYesNo),
    "nameLookAfterChildren" -> optional(nonEmptyText),
    "howMuchLookAfterChildren" -> optional(nonEmptyText verifying validCurrencyRequired),
    "howOftenLookAfterChildren" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYouLookAfterChildren" -> optional(nonEmptyText),
    "relationToPersonLookAfterChildren" -> optional(nonEmptyText),
    "payAnyoneToLookAfterPerson" -> nonEmptyText.verifying(validYesNo),
    "nameLookAfterPerson" -> optional(nonEmptyText),
    "howMuchLookAfterPerson" -> optional(nonEmptyText verifying validCurrencyRequired),
    "howOftenLookAfterPerson" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "relationToYouLookAfterPerson" -> optional(nonEmptyText),
    "relationToPersonLookAfterPerson" -> optional(nonEmptyText)
  )(AboutExpenses.apply)(AboutExpenses.unapply)
    .verifying("jobTitle.required", AboutExpenses.validateJobTitle _)
    .verifying("whatExpensesForJob.required", AboutExpenses.validateWhatExpensesForJob _)
    .verifying("nameLookAfterChildren.required", AboutExpenses.validateNameLookAfterChildren _)
    .verifying("howMuchLookAfterChildren.required", AboutExpenses.validateHowMuchLookAfterChildren _)
    .verifying("howOftenLookAfterChildren.required", AboutExpenses.validateHowOftenLookAfterChildren _)
    .verifying("relationToYouLookAfterChildren.required", AboutExpenses.validateRelationToYouLookAfterChildren _)
    .verifying("relationToPersonLookAfterChildren.required", AboutExpenses.validateRelationToPersonLookAfterChildren _)
    .verifying("nameLookAfterPerson.required", AboutExpenses.validateNameLookAfterPerson _)
    .verifying("howMuchLookAfterPerson.required", AboutExpenses.validateHowMuchLookAfterPerson _)
    .verifying("howOftenLookAfterPerson.required", AboutExpenses.validateHowOftenLookAfterPerson _)
    .verifying("relationToYouLookAfterPerson.required", AboutExpenses.validateRelationToYouLookAfterPerson _)
    .verifying("relationToPersonLookAfterPerson.required", AboutExpenses.validateRelationToPersonLookAfterPerson _)
  )

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(AboutExpenses) { implicit claim => Ok(views.html.s7_employment.g8_aboutExpenses(form.fillWithJobID(AboutExpenses, jobID))) }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("haveExpensesForJob", "error.required", FormError("haveExpensesForJob", "error.required", Seq(labelForEmployment(claim, lang, "haveExpensesForJob", jobID))))
          .replaceError("payAnyoneToLookAfterPerson", "error.required", FormError("payAnyoneToLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "payAnyoneToLookAfterPerson", jobID))))
          .replaceError("payAnyoneToLookAfterChildren", "error.required", FormError("payAnyoneToLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "payAnyoneToLookAfterChildren", jobID))))
          .replaceError("", "jobTitle.required", FormError("jobTitle", "error.required", Seq(labelForEmployment(claim, lang, "jobTitle", jobID))))
          .replaceError("", "whatExpensesForJob.required", FormError("whatExpensesForJob", "error.required", Seq(labelForEmployment(claim, lang, "whatExpensesForJob", jobID))))
          .replaceError("", "nameLookAfterChildren.required", FormError("nameLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "nameLookAfterChildren", jobID))))
          .replaceError("", "howMuchLookAfterChildren.required", FormError("howMuchLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "howMuchLookAfterChildren", jobID))))
          .replaceError("", "howOftenLookAfterChildren.required", FormError("howOftenLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "howOftenLookAfterChildren", jobID))))
          .replaceError("", "relationToYouLookAfterChildren.required", FormError("relationToYouLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "relationToYouLookAfterChildren", jobID))))
          .replaceError("", "relationToPersonLookAfterChildren.required", FormError("relationToPersonLookAfterChildren", "error.required", Seq(labelForEmployment(claim, lang, "relationToPersonLookAfterChildren", jobID))))
          .replaceError("", "nameLookAfterPerson.required", FormError("nameLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "nameLookAfterPerson", jobID))))
          .replaceError("", "howMuchLookAfterPerson.required", FormError("howMuchLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "howMuchLookAfterPerson", jobID))))
          .replaceError("", "howOftenLookAfterPerson.required", FormError("howOftenLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "howOftenLookAfterPerson", jobID))))
          .replaceError("", "relationToYouLookAfterPerson.required", FormError("relationToYouLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "relationToYouLookAfterPerson", jobID))))
          .replaceError("", "relationToPersonLookAfterPerson.required", FormError("relationToPersonLookAfterPerson", "error.required", Seq(labelForEmployment(claim, lang, "relationToPersonLookAfterPerson", jobID))))
         BadRequest(views.html.s7_employment.g8_aboutExpenses(formWithErrorsUpdate))
      },
      aboutExpenses => claim.update(jobs.update(aboutExpenses).completeJob(jobID)) -> Redirect(routes.G2BeenEmployed.present()))
  }
}