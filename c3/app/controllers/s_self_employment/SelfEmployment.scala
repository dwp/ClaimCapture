package controllers.s_self_employment

import play.api.Play._
import play.api.mvc._
import models.view.CachedClaim
import models.domain._
import models.view.Navigable
import models.view.ClaimHandling.ClaimResult
import play.api.i18n._

object SelfEmployment extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def completed = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    redirect
  }

  def completedSubmit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    redirect
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val beenInPreview = claim.checkYAnswers.previouslySavedClaim.isDefined
    val emp = claim.questionGroup[YourIncomes].getOrElse(YourIncomes())
    //Lazy because they are going to be lazily evaluated on usage only if we've been in preview.
    lazy val previousEmp = claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes]
    lazy val previousSEValue = previousEmp.get.beenSelfEmployedSince1WeekBeforeClaim
    val SEValue = emp.beenSelfEmployedSince1WeekBeforeClaim
                                                 //This part of the condition has been removed due to review on user story about enabling changing employment/self-employment on check your answers
    if (models.domain.SelfEmployment.visible ) c //&& (!beenInPreview || beenInPreview && SEValue == yes && previousSEValue == no)) c
    else redirect
  }

  private def redirect()(implicit claim: Claim, lang: Lang, messages: Messages, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.your_income.routes.GStatutorySickPay.present())
}
