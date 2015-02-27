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
import controllers.mappings.Mappings._
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

  val payForThings =
    "payForThings" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersTextWithPound(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payForThings.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "iterationID" -> nonEmptyText,
    payPensionScheme,
    payForThings,
    haveExpensesForJob
  )(PensionAndExpenses.apply)(PensionAndExpenses.unapply))


  def present(iterationID: String) = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    track(PensionAndExpenses) { implicit claim => Ok(views.html.s7_employment.g8_pensionAndExpenses(form.fillWithJobID(PensionAndExpenses, iterationID))(lang)) }
  }

  def submit = claimingWithCheckInIteration { iterationID => implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payPensionScheme.answer","error.required",FormError("payPensionScheme.answer","error.required", Seq(labelForEmployment(claim, lang, "payPensionScheme.answer", iterationID))))
          .replaceError("payPensionScheme","payPensionScheme.text.required",FormError("payPensionScheme.text","error.required", Seq(labelForEmployment(claim, lang, "payPensionScheme.text", iterationID))))
          .replaceError("payPensionScheme","payPensionScheme.text.maxLength",FormError("payPensionScheme.text","error.maxLength", Seq(labelForEmployment(claim, lang, "payPensionScheme.text", iterationID))))
          .replaceError("payPensionScheme.text",errorRestrictedCharacters,FormError("payPensionScheme.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, lang, "payPensionScheme.text", iterationID))))
          .replaceError("payForThings.answer","error.required",FormError("payForThings.answer","error.required", Seq(labelForEmployment(claim, lang, "payForThings.answer", iterationID))))
          .replaceError("payForThings","payForThings.text.required",FormError("payForThings.text","error.required", Seq(labelForEmployment(claim, lang, "payForThings.text", iterationID))))
          .replaceError("payForThings","payForThings.text.maxLength",FormError("payForThings.text","error.maxLength", Seq(labelForEmployment(claim, lang, "payForThings.text", iterationID))))
          .replaceError("payForThings.text",errorRestrictedCharacters,FormError("payForThings.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, lang, "payForThings.text", iterationID))))
          .replaceError("haveExpensesForJob.answer","error.required",FormError("haveExpensesForJob.answer","error.required", Seq(labelForEmployment(claim, lang, "haveExpensesForJob.answer", iterationID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.required",FormError("haveExpensesForJob.text","error.required", Seq(labelForEmployment(claim, lang, "haveExpensesForJob.text", iterationID))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text..maxLength",FormError("haveExpensesForJob.text","error.maxLength", Seq(labelForEmployment(claim, lang, "haveExpensesForJob.text", iterationID))))
          .replaceError("haveExpensesForJob.text",errorRestrictedCharacters,FormError("haveExpensesForJob.text",errorRestrictedCharacters, Seq(labelForEmployment(claim, lang, "haveExpensesForJob.text", iterationID))))

          BadRequest(views.html.s7_employment.g8_pensionAndExpenses(formWithErrorsUpdate)(lang))
      },
      aboutExpenses => claim.update(jobs.update(aboutExpenses).completeJob(iterationID)) -> Redirect(routes.G2BeenEmployed.present()))

  }
}