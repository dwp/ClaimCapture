package controllers.s9_self_employment


import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentYourAccounts, Claim}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import play.api.data.Form


object G2SelfEmploymentYourAccounts extends Controller with CachedClaim{
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(SelfEmploymentYourAccounts)

  val form = Form(
    mapping(
      call(routes.G2SelfEmploymentYourAccounts.present()),
      "whatWasOrIsYourTradingYearFrom" -> optional(dayMonthYear.verifying(validDateOnly)),
      "whatWasOrIsYourTradingYearTo" -> optional(dayMonthYear.verifying(validDateOnly)),
      "areAccountsPreparedOnCashFlowBasis" -> nonEmptyText.verifying(validYesNo),
      "areIncomeOutgoingsProfitSimilarToTrading" -> optional(nonEmptyText.verifying(validYesNo)),
      "tellUsWhyAndWhenTheChangeHappened" -> nonEmptyText(maxLength = 300),
      "doYouHaveAnAccountant" -> optional(nonEmptyText.verifying(validYesNo)),
      "canWeContactYourAccountant" -> nonEmptyText.verifying(validYesNo)
    )(SelfEmploymentYourAccounts.apply)(SelfEmploymentYourAccounts.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_self_employment.g2_selfEmploymentYourAccounts(form, completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g2_selfEmploymentYourAccounts(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G2SelfEmploymentYourAccounts.present())
      )
  }
}
