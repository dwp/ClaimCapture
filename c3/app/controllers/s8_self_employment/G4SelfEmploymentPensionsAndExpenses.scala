package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.Mappings._
import models.domain.SelfEmploymentPensionsAndExpenses
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment._
import utils.helpers.PastPresentLabelHelper._
import models.view.Navigable
import play.api.i18n.Lang
import play.api.data.FormError
import models.domain.Claim
import models.view.CachedClaim.ClaimResult

object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim with Navigable {
  def form(implicit claim: Claim) = Form(mapping(
    "doYouPayToPensionScheme" -> nonEmptyText.verifying(validYesNo),
    "howMuchDidYouPay" -> optional(nonEmptyText verifying validCurrencyRequired),
    "howOften" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "doYouPayToLookAfterYourChildren" -> nonEmptyText.verifying(validYesNo),
    "didYouPayToLookAfterThePersonYouCaredFor" -> nonEmptyText.verifying(validYesNo)
  )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply)
    .verifying("howMuchDidYouPay", SelfEmploymentPensionsAndExpenses.validateHowMuchSelfEmployed _)
    .verifying("howOften.required", SelfEmploymentPensionsAndExpenses.validateHowOftenSelfEmployed _))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    presentConditionally(selfEmploymentYourAccounts)
  }

  def selfEmploymentYourAccounts(implicit claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult = {
    track(SelfEmploymentPensionsAndExpenses) { implicit claim => Ok(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses))) }
  }
  
  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("doYouPayToPensionScheme", "error.required", FormError("doYouPayToPensionScheme.answer", "error.required", Seq(labelForSelfEmployment(claim, lang, "doYouPayToPensionScheme.answer"))))
          .replaceError("", "howMuchDidYouPay", FormError("howMuchDidYouPay", "error.required", Seq(labelForSelfEmployment(claim, lang, "howMuchDidYouPay"))))
          .replaceError("", "howOften.required", FormError("howOften", "error.required", Seq(labelForSelfEmployment(claim, lang, "howOften"))))
          .replaceError("howMuchDidYouPay", "decimal.invalid", FormError("howMuchDidYouPay", "decimal.invalid", Seq(labelForSelfEmployment(claim, lang, "howMuchDidYouPay"))))
          .replaceError("howOften", "error.paymentFrequency", FormError("howOften", "error.paymentFrequency", Seq(labelForSelfEmployment(claim, lang, "howOften"))))
          .replaceError("doYouPayToLookAfterYourChildren", "error.required", FormError("doYouPayToLookAfterYourChildren", "error.required", Seq(labelForSelfEmployment(claim, lang, "doYouPayToLookAfterYourChildren"))))
          .replaceError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", FormError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", Seq(labelForSelfEmployment(claim, lang, "didYouPayToLookAfterThePersonYouCaredFor"))))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength",Seq("60",labelForSelfEmployment(claim, lang, "howOften"))))

        BadRequest(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.G5ChildcareExpensesWhileAtWork.present()))
  }
}