package controllers.s_self_employment

import play.api.Play._

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
import controllers.s_self_employment.SelfEmployment._
import utils.helpers.PastPresentLabelHelper._
import models.view.Navigable
import models.view.ClaimHandling.ClaimResult
import controllers.CarersForms._
import play.api.data.FormError
import models.yesNo.YesNoWithText
import play.api.i18n._

object GSelfEmploymentPensionsAndExpenses extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val payPensionScheme =
    "payPensionScheme" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("payPensionScheme.text.required", YesNoWithText.validateOnYes _)

  val haveExpensesForJob =
    "haveExpensesForJob" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("haveExpensesForJob.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    payPensionScheme,
    haveExpensesForJob
  )(SelfEmploymentPensionsAndExpenses.apply)(SelfEmploymentPensionsAndExpenses.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(selfEmploymentYourAccounts)
  }

  def selfEmploymentYourAccounts(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(SelfEmploymentPensionsAndExpenses) { implicit claim => Ok(views.html.s_self_employment.g_selfEmploymentPensionAndExpenses(form.fill(SelfEmploymentPensionsAndExpenses))) }
  }
  
  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("payPensionScheme.answer",errorRequired,FormError("payPensionScheme.answer",errorRequired, Seq(labelForSelfEmployment(claim, request2lang, "payPensionScheme.answer"))))
          .replaceError("payPensionScheme","payPensionScheme.text.required",FormError("payPensionScheme.text",errorRequired, Seq(labelForSelfEmployment(claim, request2lang, "payPensionScheme.text"))))
          .replaceError("payPensionScheme","payPensionScheme.text.maxLength",FormError("payPensionScheme.text",maxLengthError, Seq(labelForSelfEmployment(claim, request2lang, "payPensionScheme.text"))))
          .replaceError("payPensionScheme.text",errorRestrictedCharacters,FormError("payPensionScheme.text",errorRestrictedCharacters, Seq(labelForSelfEmployment(claim, request2lang, "payPensionScheme.text"))))
          .replaceError("haveExpensesForJob.answer",errorRequired,FormError("haveExpensesForJob.answer",errorRequired, Seq(labelForSelfEmployment(claim, request2lang, "haveExpensesForJob.answer"))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.required",FormError("haveExpensesForJob.text",errorRequired, Seq(labelForSelfEmployment(claim, request2lang, "haveExpensesForJob.text"))))
          .replaceError("haveExpensesForJob","haveExpensesForJob.text.maxLength",FormError("haveExpensesForJob.text",maxLengthError, Seq(labelForSelfEmployment(claim, request2lang, "haveExpensesForJob.text"))))
          .replaceError("haveExpensesForJob.text",errorRestrictedCharacters,FormError("haveExpensesForJob.text",errorRestrictedCharacters, Seq(labelForSelfEmployment(claim, request2lang, "haveExpensesForJob.text"))))

        BadRequest(views.html.s_self_employment.g_selfEmploymentPensionAndExpenses(formWithErrorsUpdate))
      },
      f => claim.update(f) ->  Redirect(routes.SelfEmployment.completedSubmit())
    )
  }
}
