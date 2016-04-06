package controllers.your_income

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{YourIncomes, Claim, StatutorySickPay}
import models.view.ClaimHandling._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.{AnyContent, Request, Controller}
import utils.helpers.CarersForm._
import controllers.CarersForms._

/**
  * Created by peterwhitehead on 24/03/2016.
  */
object GStatutorySickPay extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "stillBeingPaidThisPay" -> carersNonEmptyText.verifying(validYesNo),
    "whenDidYouLastGetPaid" -> optional(dayMonthYear.verifying(validDate)),
    "whoPaidYouThisPay" -> carersNonEmptyText(maxLength = Mappings.sixty),
    "amountOfThisPay" -> nonEmptyText.verifying(validCurrency8Required),
    "howOftenPaidThisPay" -> carersNonEmptyText.verifying(validPaymentFrequency),
    "howOftenPaidThisPayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty))
  )(StatutorySickPay.apply)(StatutorySickPay.unapply)
    .verifying(StatutorySickPay.howOftenPaidThisPayItVariesRequired)
    .verifying(StatutorySickPay.whenDidYouLastGetPaidRequired)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(statutorySickPay)
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "howOftenPaidThisPay.required", FormError("howOftenPaidThisPayOther", errorRequired))
          .replaceError("", "whenDidYouLastGetPaid.required", FormError("whenDidYouLastGetPaid", errorRequired))
        BadRequest(views.html.your_income.statutorySickPay(formWithErrorsUpdate))
      },
      statutorySickPay => claim.update(statutorySickPay) -> Redirect(controllers.your_income.routes.GStatutoryMaternityPaternityAdoptionPay.present()))
  }.withPreviewConditionally[YourIncomes](checkGoPreview)

  def statutorySickPay(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(StatutorySickPay) { implicit claim => Ok(views.html.your_income.statutorySickPay(form.fill(StatutorySickPay))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val previousYourIncome = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    if (previousYourIncome.yourIncome_sickpay != yourIncomes.yourIncome_sickpay && yourIncomes.yourIncome_sickpay.isDefined && models.domain.YourIncomeStatutorySickPay.visible) c
    else claim -> Redirect(controllers.your_income.routes.GStatutoryMaternityPaternityAdoptionPay.present())
  }

  private def checkGoPreview(t:(Option[YourIncomes], YourIncomes), c:(Option[Claim],Claim)): Boolean = {
    val previousEmp = t._1.get
    val currentEmp = t._2
    val statutorySickChanged = !previousEmp.yourIncome_patmatadoppay.isDefined && currentEmp.yourIncome_patmatadoppay.isDefined
    val fosteringAllowanceChanged = !previousEmp.yourIncome_fostering.isDefined && currentEmp.yourIncome_fostering.isDefined
    val directPaymentChanged = !previousEmp.yourIncome_directpay.isDefined && currentEmp.yourIncome_directpay.isDefined
    val otherPaymentsChanged = !previousEmp.yourIncome_anyother.isDefined && currentEmp.yourIncome_anyother.isDefined
    !(statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)

    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
  }
}