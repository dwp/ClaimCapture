package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import controllers.Mappings._
import models.domain._
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment._
import utils.helpers.PastPresentLabelHelper._
import models.view.Navigable

object G7ExpensesWhileAtWork extends Controller with CachedClaim with Navigable {
  def form(implicit claim: DigitalForm) = Form(
    mapping(
      "nameOfPerson" -> nonEmptyText(maxLength = sixty),
      "howMuchYouPay" -> nonEmptyText(maxLength = 8).verifying(validDecimalNumber),
      "howOftenPayExpenses" -> (pensionPaymentFrequency verifying validPensionPaymentFrequencyOnly),
      "whatRelationIsToYou" -> nonEmptyText(maxLength = sixty),
      "relationToPartner" -> optional(nonEmptyText(maxLength = sixty)),
      "whatRelationIsTothePersonYouCareFor" -> nonEmptyText
    )(ExpensesWhileAtWork.apply)(ExpensesWhileAtWork.unapply)
      .verifying("relationToPartner.required", validateRelationToPartner(claim, _)))

  def validateRelationToPartner(implicit claim: DigitalForm, expensesWhileAtWork: ExpensesWhileAtWork) = {
    claim.questionGroup(MoreAboutYou) -> claim.questionGroup(PersonYouCareFor) match {
      case (Some(m: MoreAboutYou), Some(p: PersonYouCareFor)) if m.hadPartnerSinceClaimDate == "yes" && p.isPartnerPersonYouCareFor == "no" => expensesWhileAtWork.relationToPartner.isDefined
      case _ => true
    }
  }

  def present = executeOnForm { implicit claim => implicit request =>
    presentConditionally(expensesWhileAtWork)
  }

  def expensesWhileAtWork(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    val payToLookPersonYouCareFor = claim.questionGroup(SelfEmploymentPensionsAndExpenses) match {
      case Some(s: SelfEmploymentPensionsAndExpenses) => s.didYouPayToLookAfterThePersonYouCaredFor == `yes`
      case _ => false
    }

    payToLookPersonYouCareFor match {
      case true => track(ExpensesWhileAtWork) { implicit claim => Ok(views.html.s8_self_employment.g7_expensesWhileAtWork(form.fill(ExpensesWhileAtWork)))}
      case false => claim.delete(ExpensesWhileAtWork) ->  Redirect(routes.SelfEmployment.completed())
    }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchYouPay", "error.required", FormError("howMuchYouPay", "error.required", Seq(didYouDoYouIfSelfEmployed.toLowerCase)))
          .replaceError("howMuchYouPay", "decimal.invalid", FormError("howMuchYouPay", "decimal.invalid", Seq(didYouDoYouIfSelfEmployed.toLowerCase)))
          .replaceError("howOftenPayExpenses", "error.required", FormError("howOftenPayExpenses", "error.required", Seq(didYouDoYouIfSelfEmployed.toLowerCase)))
          .replaceError("", "relationToPartner.required", FormError("relationToPartner", "error.required"))
        BadRequest(views.html.s8_self_employment.g7_expensesWhileAtWork(formWithErrorsUpdate))
      },
      f => claim.update(f) ->  Redirect(routes.SelfEmployment.completed())
    )
  }
}