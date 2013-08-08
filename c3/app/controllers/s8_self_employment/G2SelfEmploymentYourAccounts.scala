package controllers.s8_self_employment


import language.reflectiveCalls
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentYourAccounts, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import play.api.data.{FormError, Form}
import SelfEmployment.whenSectionVisible


object G2SelfEmploymentYourAccounts extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentYourAccounts)

  val form = Form(
    mapping(
      call(routes.G2SelfEmploymentYourAccounts.present()),
      "whatWasOrIsYourTradingYearFrom" -> optional(dayMonthYear.verifying(validDateOnly)),
      "whatWasOrIsYourTradingYearTo" -> optional(dayMonthYear.verifying(validDateOnly)),
      "areAccountsPreparedOnCashFlowBasis" -> nonEmptyText.verifying(validYesNo),
      "areIncomeOutgoingsProfitSimilarToTrading" -> optional(text verifying (validYesNo)),
      "tellUsWhyAndWhenTheChangeHappened" -> optional(nonEmptyText(maxLength = 300)),
      "doYouHaveAnAccountant" -> optional(text verifying (validYesNo)),
      "canWeContactYourAccountant" -> optional(nonEmptyText.verifying(validYesNo))
    )(SelfEmploymentYourAccounts.apply)(SelfEmploymentYourAccounts.unapply)
      .verifying("tellUsWhyAndWhenTheChangeHappened", validateChangeHappened _)
      .verifying("canWeContactYourAccountant", validateContactAccountant _)
  )

  def validateChangeHappened(selfEmploymentYourAccounts: SelfEmploymentYourAccounts) = {
    selfEmploymentYourAccounts.areIncomeOutgoingsProfitSimilarToTrading match {
      case Some(`no`) => selfEmploymentYourAccounts.tellUsWhyAndWhenTheChangeHappened.isDefined
      case Some(`yes`) => true
      case _ => true
    }
  }

  def validateContactAccountant(selfEmploymentYourAccounts: SelfEmploymentYourAccounts) = {
    selfEmploymentYourAccounts.doYouHaveAnAccountant match {
      case Some(`yes`) => selfEmploymentYourAccounts.canWeContactYourAccountant.isDefined
      case Some(`no`) => true
      case _ => true
    }
  }

  def present = claiming {
    implicit claim => implicit request =>
      whenSectionVisible(Ok(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(form.fill(SelfEmploymentYourAccounts), completedQuestionGroups)))
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => {
            val formWithErrorsUpdate = formWithErrors
              .replaceError("tellUsWhyAndWhenTheChangeHappened", FormError("tellUsWhyAndWhenTheChangeHappened", "error.required"))
              .replaceError("canWeContactYourAccountant", FormError("canWeContactYourAccountant", "error.required"))
            BadRequest(views.html.s8_self_employment.g2_selfEmploymentYourAccounts(formWithErrorsUpdate, completedQuestionGroups))
          },
          f => claim.update(f) -> Redirect(routes.G3SelfEmploymentAccountantContactDetails.present())
        )
  }
}
