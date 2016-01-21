package controllers.s_self_employment

import controllers.mappings.Mappings
import play.api.Play._

import language.reflectiveCalls
import play.api.data.Forms._
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.data.Form
import controllers.mappings.Mappings._
import models.domain.SelfEmploymentYourAccounts
import models.domain.{Employment => Emp}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import SelfEmployment._
import models.view.Navigable
import controllers.CarersForms._
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import models.domain.Claim
import models.view.ClaimHandling.ClaimResult
import play.api.i18n._

object GSelfEmploymentYourAccounts extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "doYouKnowYourTradingYear" -> carersNonEmptyText.verifying(validYesNo),
    "whatWasOrIsYourTradingYearFrom" -> optional(dayMonthYear.verifying(validDateOnly)),
    "whatWasOrIsYourTradingYearTo" -> optional(dayMonthYear.verifying(validDateOnly)),
    "areIncomeOutgoingsProfitSimilarToTrading" -> optional(text verifying validYesNo),
    "tellUsWhyAndWhenTheChangeHappened" -> optional(carersNonEmptyText(maxLength = 300))
  )(SelfEmploymentYourAccounts.apply)(SelfEmploymentYourAccounts.unapply)
    .verifying("required", validateChangeHappened _))

  private def validateChangeHappened(selfEmploymentYourAccounts: SelfEmploymentYourAccounts) = {
    selfEmploymentYourAccounts.areIncomeOutgoingsProfitSimilarToTrading match {
      case Some(`no`) => selfEmploymentYourAccounts.tellUsWhyAndWhenTheChangeHappened.isDefined
      case Some(`yes`) => true
      case _ => true
    }
  }

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(selfEmploymentYourAccounts)
  }

  def selfEmploymentYourAccounts(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(SelfEmploymentYourAccounts) { implicit claim => Ok(views.html.s_self_employment.g_selfEmploymentYourAccounts(form.fill(SelfEmploymentYourAccounts))) }
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("","required", FormError("tellUsWhyAndWhenTheChangeHappened", Mappings.errorRequired))
          .replaceError("whatWasOrIsYourTradingYearFrom","error.invalid", FormError("whatWasOrIsYourTradingYearFrom", "error.invalid", Seq(labelForSelfEmployment(claim, request2lang, "whatWasOrIsYourTradingYearFrom"))))
          .replaceError("whatWasOrIsYourTradingYearFrom.year","error.number", FormError("whatWasOrIsYourTradingYearFrom.year", "error.number", Seq(labelForSelfEmployment(claim, request2lang, "whatWasOrIsYourTradingYearFrom.year"))))
          .replaceError("whatWasOrIsYourTradingYearTo","error.invalid", FormError("whatWasOrIsYourTradingYearTo", "error.invalid", Seq(labelForSelfEmployment(claim, request2lang, "whatWasOrIsYourTradingYearTo"))))
          .replaceError("whatWasOrIsYourTradingYearTo.year","error.number", FormError("whatWasOrIsYourTradingYearTo.year", "error.number", Seq(labelForSelfEmployment(claim, request2lang, "whatWasOrIsYourTradingYearTo.year"))))
        BadRequest(views.html.s_self_employment.g_selfEmploymentYourAccounts(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.GSelfEmploymentPensionsAndExpenses.present()))
  }
}
