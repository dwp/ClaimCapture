package controllers.your_income

import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{YourIncomes, StatutoryMaternityPaternityAdoptionPay, Claim}
import models.view.ClaimHandling._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.{AnyContent, Controller, Request}
import utils.helpers.CarersForm._
import utils.helpers.ReturnToSummaryHelper

/**
  * Created by peterwhitehead on 24/03/2016.
  */
object GStatutoryMaternityPaternityAdoptionPay extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "paymentTypesForThisPay" -> carersNonEmptyText.verifying(validPaymentType),
    "stillBeingPaidThisPay_paternityMaternityAdoption" -> carersNonEmptyText.verifying(validYesNo),
    "whenDidYouLastGetPaid" -> optional(dayMonthYear.verifying(validDate)),
    "whoPaidYouThisPay_paternityMaternityAdoption" -> carersNonEmptyText(maxLength = Mappings.sixty),
    "amountOfThisPay" -> nonEmptyText.verifying(validCurrency8Required),
    "howOftenPaidThisPay" -> carersNonEmptyText.verifying(validPaymentFrequency),
    "howOftenPaidThisPayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty))
  )(StatutoryMaternityPaternityAdoptionPay.apply)(StatutoryMaternityPaternityAdoptionPay.unapply)
    .verifying(StatutoryMaternityPaternityAdoptionPay.howOftenPaidThisPayItVariesRequired)
    .verifying(StatutoryMaternityPaternityAdoptionPay.whenDidYouLastGetPaidRequired)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(statutoryMaternityPaternityPay)
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "howOftenPaidThisPay.required", FormError("howOftenPaidThisPayOther", errorRequired))
          .replaceError("", "whenDidYouLastGetPaid.required", FormError("whenDidYouLastGetPaid", errorRequired))
        BadRequest(views.html.your_income.statutoryMaternityPaternityAdoptionPay(formWithErrorsUpdate))
      },
      statutoryMaternityPaternityAdoptionPay => claim.update(statutoryMaternityPaternityAdoptionPay) -> Redirect(controllers.your_income.routes.GFosteringAllowance.present()))
  }.withPreviewConditionally[YourIncomes](checkGoPreview)

  def statutoryMaternityPaternityPay(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(StatutoryMaternityPaternityAdoptionPay) { implicit claim => Ok(views.html.your_income.statutoryMaternityPaternityAdoptionPay(form.fill(StatutoryMaternityPaternityAdoptionPay))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val previousYourIncome = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(YourIncomes())
    if (((previousYourIncome.yourIncome_patmatadoppay != yourIncomes.yourIncome_patmatadoppay || claim.questionGroup[StatutoryMaternityPaternityAdoptionPay].getOrElse(StatutoryMaternityPaternityAdoptionPay()).whoPaidYouThisPay.isEmpty) && yourIncomes.yourIncome_patmatadoppay.isDefined || !request.flash.isEmpty) && models.domain.YourIncomeStatutoryMaternityPaternityAdoptionPay.visible) c
    else claim -> Redirect(controllers.your_income.routes.GFosteringAllowance.present())
  }

  private def checkGoPreview(t:(Option[YourIncomes], YourIncomes), c:(Option[Claim],Claim)): Boolean = {
    val currentClaim = c._2
    ReturnToSummaryHelper.haveOtherPaymentsFromStatutoryPayChanged(currentClaim)

    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
  }
}