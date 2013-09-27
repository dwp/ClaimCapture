package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.data.FormError
import controllers.Mappings._
import models.domain.{Claim, SelfEmploymentPensionsAndExpenses}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment._
import utils.helpers.PastPresentLabelHelper.didYouDoYouIfSelfEmployed
import models.view.Navigable

object G4SelfEmploymentPensionsAndExpenses extends Controller with CachedClaim with Navigable {
  def form(implicit claim: Claim) = Form(mapping(
    "doYouPayToPensionScheme" -> nonEmptyText.verifying(validYesNo),
    "howMuchDidYouPay" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOften" -> optional(pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
    "doYouPayToLookAfterYourChildren" -> nonEmptyText.verifying(validYesNo),
    "didYouPayToLookAfterThePersonYouCaredFor" -> nonEmptyText.verifying(validYesNo)
  )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply)
    .verifying("howMuchDidYouPay", SelfEmploymentPensionsAndExpenses.validateHowMuchSelfEmployed _)
    .verifying("howOften", SelfEmploymentPensionsAndExpenses.validateHowOftenSelfEmployed _))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally(selfEmploymentYourAccounts)
  }

  def selfEmploymentYourAccounts(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(SelfEmploymentPensionsAndExpenses) { implicit claim => Ok(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses))) }
  }
  
  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresent = didYouDoYouIfSelfEmployed
        val formWithErrorsUpdate = formWithErrors
          .replaceError("doYouPayToPensionScheme", "error.required", FormError("doYouPayToPensionScheme.answer", "error.required", Seq(pastPresent)))
          .replaceError("", "howMuchDidYouPay", FormError("howMuchDidYouPay", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("howMuchDidYouPay", "decimal.invalid", FormError("howMuchDidYouPay", "decimal.invalid", Seq(pastPresent.toLowerCase)))
          .replaceError("howOften", "error.paymentFrequency", FormError("doYouPayToPensionScheme.howOften", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("doYouPayToLookAfterYourChildren", "error.required", FormError("doYouPayToLookAfterYourChildren", "error.required", Seq(pastPresent.toLowerCase)))
          .replaceError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", FormError("didYouPayToLookAfterThePersonYouCaredFor", "error.required", Seq(pastPresent.toLowerCase)))
        BadRequest(views.html.s8_self_employment.g4_selfEmploymentPensionsAndExpenses(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.G5ChildcareExpensesWhileAtWork.present()))
  }
}