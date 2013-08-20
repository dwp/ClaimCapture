package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.SelfEmploymentYourAccounts
import models.view.CachedClaim
import utils.helpers.CarersForm._
import play.api.data.{FormError, Form}
import SelfEmployment.whenSectionVisible

object G2SelfEmploymentYourAccounts extends Controller with SelfEmploymentRouting with CachedClaim {
  val form = Form(
    mapping(
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

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(form.fill(SelfEmploymentYourAccounts), completedQuestionGroups(SelfEmploymentYourAccounts))))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("tellUsWhyAndWhenTheChangeHappened", FormError("tellUsWhyAndWhenTheChangeHappened", "error.required"))
        BadRequest(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(formWithErrorsUpdate, completedQuestionGroups(SelfEmploymentYourAccounts)))
      },
      f => claim.update(f) -> Redirect(routes.G4SelfEmploymentPensionsAndExpenses.present()))
  }
}
