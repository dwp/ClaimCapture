package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.mappings.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment._
import utils.helpers.PastPresentLabelHelper._
import models.view.Navigable
import play.api.i18n.Lang
import models.view.CachedClaim.ClaimResult
import controllers.CarersForms._
import play.api.data.FormError
import models.yesNo.YesNoWithText

object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim with Navigable {
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
    payPensionScheme,
    haveExpensesForJob
  )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply))

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    presentConditionally(selfEmploymentYourAccounts(lang),lang)
  }

  def selfEmploymentYourAccounts( lang: Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(SelfEmploymentPensionsAndExpenses) { implicit claim => Ok(views.html.s8_self_employment.g4_selfEmploymentPensionAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses))(lang)) }
  }
  
  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payPensionScheme.answer","error.required",FormError("payPensionScheme.answer","error.required", Seq(labelForSelfEmployment(claim, lang, "payPensionScheme.answer"))))
          .replaceError("payPensionScheme","payPensionScheme.text.required",FormError("payPensionScheme.text","error.required", Seq(labelForSelfEmployment(claim, lang, "payPensionScheme.text"))))
          .replaceError("payPensionScheme","payPensionScheme.text.maxLength",FormError("payPensionScheme.text","error.maxLength", Seq(labelForSelfEmployment(claim, lang, "payPensionScheme.text"))))
          .replaceError("payPensionScheme.text",errorRestrictedCharacters,FormError("payPensionScheme.text",errorRestrictedCharacters, Seq(labelForSelfEmployment(claim, lang, "payPensionScheme.text"))))
          .replaceError("haveExpensesForJob.answer","error.required",FormError("haveExpensesForJob.answer","error.required", Seq(labelForSelfEmployment(claim, lang, "haveExpensesForJob.answer"))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.required",FormError("haveExpensesForJob.text","error.required", Seq(labelForSelfEmployment(claim, lang, "haveExpensesForJob.text"))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.maxLength",FormError("haveExpensesForJob.text","error.maxLength", Seq(labelForSelfEmployment(claim, lang, "haveExpensesForJob.text"))))
          .replaceError("haveExpensesForJob.text",errorRestrictedCharacters,FormError("haveExpensesForJob.text",errorRestrictedCharacters, Seq(labelForSelfEmployment(claim, lang, "haveExpensesForJob.text"))))

        BadRequest(views.html.s8_self_employment.g4_selfEmploymentPensionAndExpenses(formWithErrorsUpdate)(lang))
      },
      f => claim.update(f) ->  Redirect(routes.SelfEmployment.completedSubmit())
    )
  }
}