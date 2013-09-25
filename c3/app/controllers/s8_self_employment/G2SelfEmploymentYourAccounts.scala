package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.data.{FormError, Form}
import controllers.Mappings._
import models.domain.SelfEmploymentYourAccounts
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable
import models.domain.DigitalForm

object G2SelfEmploymentYourAccounts extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "whatWasOrIsYourTradingYearFrom" -> optional(dayMonthYear.verifying(validDateOnly)),
    "whatWasOrIsYourTradingYearTo" -> optional(dayMonthYear.verifying(validDateOnly)),
    "areIncomeOutgoingsProfitSimilarToTrading" -> optional(text verifying validYesNo),
    "tellUsWhyAndWhenTheChangeHappened" -> optional(nonEmptyText(maxLength = 300))
  )(SelfEmploymentYourAccounts.apply)(SelfEmploymentYourAccounts.unapply)
    .verifying("tellUsWhyAndWhenTheChangeHappened", validateChangeHappened _))

  def validateChangeHappened(selfEmploymentYourAccounts: SelfEmploymentYourAccounts) = {
    selfEmploymentYourAccounts.areIncomeOutgoingsProfitSimilarToTrading match {
      case Some(`no`) => selfEmploymentYourAccounts.tellUsWhyAndWhenTheChangeHappened.isDefined
      case Some(`yes`) => true
      case _ => true
    }
  }

  def present = executeOnForm { implicit claim => implicit request =>
    presentConditionally(selfEmploymentYourAccounts)
  }

  def selfEmploymentYourAccounts(implicit claim: DigitalForm, request: Request[AnyContent]): FormResult = {
    track(SelfEmploymentYourAccounts) { implicit claim => Ok(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(form.fill(SelfEmploymentYourAccounts))) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("tellUsWhyAndWhenTheChangeHappened", FormError("tellUsWhyAndWhenTheChangeHappened", "error.required"))
        BadRequest(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.G4SelfEmploymentPensionsAndExpenses.present()))
  }
}