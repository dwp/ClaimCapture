package controllers.s_employment

import play.api.Play._

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{BeenEmployed, PensionAndExpenses}
import utils.helpers.CarersForm._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import controllers.mappings.Mappings._
import play.api.data.FormError
import controllers.CarersForms._
import models.yesNo.YesNoWithText
import play.api.i18n._

object GPensionAndExpenses extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val payPensionScheme =
    "payPensionScheme" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payPensionScheme.text.required", YesNoWithText.validateOnYes _)

  val haveExpensesForJob =
    "haveExpensesForJob" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("haveExpensesForJob.text.required", YesNoWithText.validateOnYes _)

  val payForThings =
    "payForThings" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payForThings.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "iterationID" -> nonEmptyText,
    payPensionScheme,
    payForThings,
    haveExpensesForJob
  )(PensionAndExpenses.apply)(PensionAndExpenses.unapply))


  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(PensionAndExpenses) { implicit claim => Ok(views.html.s_employment.g_pensionAndExpenses(form.fillWithJobID(PensionAndExpenses, iterationID))) }
  }

  def submit = claimingWithCheckInIteration { iterationID => implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payPensionScheme.answer",errorRequired,FormError("payPensionScheme.answer",errorRequired, Seq(labelForEmployment(claim, request2lang, "payPensionScheme.answer", iterationID))))
          .replaceError("payPensionScheme","payPensionScheme.text.required",FormError("payPensionScheme.text",errorRequired, Seq(labelForEmployment(claim, request2lang, "payPensionScheme.text", iterationID))))
          .replaceError("payPensionScheme","payPensionScheme.text.maxLength",FormError("payPensionScheme.text",maxLengthError, Seq(labelForEmployment(claim, request2lang, "payPensionScheme.text", iterationID))))
          .replaceError("payPensionScheme.text",errorRestrictedCharacters,FormError("payPensionScheme.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, request2lang, "payPensionScheme.text", iterationID))))
          .replaceError("payForThings.answer",errorRequired,FormError("payForThings.answer",errorRequired, Seq(labelForEmployment(claim, request2lang, "payForThings.answer", iterationID))))
          .replaceError("payForThings","payForThings.text.required",FormError("payForThings.text",errorRequired, Seq(labelForEmployment(claim, request2lang, "payForThings.text", iterationID))))
          .replaceError("payForThings","payForThings.text.maxLength",FormError("payForThings.text",maxLengthError, Seq(labelForEmployment(claim, request2lang, "payForThings.text", iterationID))))
          .replaceError("payForThings.text",errorRestrictedCharacters,FormError("payForThings.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, request2lang, "payForThings.text", iterationID))))
          .replaceError("haveExpensesForJob.answer",errorRequired,FormError("haveExpensesForJob.answer",errorRequired, Seq(labelForEmployment(claim, request2lang, "haveExpensesForJob.answer", iterationID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.required",FormError("haveExpensesForJob.text",errorRequired, Seq(labelForEmployment(claim, request2lang, "haveExpensesForJob.text", iterationID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text..maxLength",FormError("haveExpensesForJob.text",maxLengthError, Seq(labelForEmployment(claim, request2lang, "haveExpensesForJob.text", iterationID))))
          .replaceError("haveExpensesForJob.text",errorRestrictedCharacters,FormError("haveExpensesForJob.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, request2lang, "haveExpensesForJob.text", iterationID))))

          BadRequest(views.html.s_employment.g_pensionAndExpenses(formWithErrorsUpdate))
      },
      // Must delete the BeenEmployed question group so it doesn't prepopulate the
      // question 'Have you had any more employments...'
      aboutExpenses => claim.update(jobs.update(aboutExpenses).completeJob(iterationID)).delete(BeenEmployed) -> Redirect(routes.GBeenEmployed.present()))

  }
}
