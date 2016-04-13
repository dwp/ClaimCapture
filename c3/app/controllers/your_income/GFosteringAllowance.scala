package controllers.your_income

import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{YourIncomes, FosteringAllowance, Claim}
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
object GFosteringAllowance extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "fosteringAllowancePay" -> carersNonEmptyText.verifying(validFosteringAllowancePaymentType),
    "fosteringAllowancePayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty)),
    "stillBeingPaidThisPay_fosteringAllowance" -> carersNonEmptyText.verifying(validYesNo),
    "whenDidYouLastGetPaid" -> optional(dayMonthYear.verifying(validDate)),
    "whoPaidYouThisPay_fosteringAllowance" -> carersNonEmptyText(maxLength = Mappings.sixty),
    "amountOfThisPay" -> nonEmptyText.verifying(validCurrency8Required),
    "howOftenPaidThisPay" -> carersNonEmptyText.verifying(validPaymentFrequency),
    "howOftenPaidThisPayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty))
  )(FosteringAllowance.apply)(FosteringAllowance.unapply)
    .verifying(FosteringAllowance.paymentTypesForThisPayOtherRequired)
    .verifying(FosteringAllowance.howOftenPaidThisPayItVariesRequired)
    .verifying(FosteringAllowance.whenDidYouLastGetPaidRequired)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    presentConditionally(fosteringAllowance)
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "paymentTypesForThisPayOther.required", FormError("fosteringAllowancePayOther", errorRequired))
          .replaceError("", "howOftenPaidThisPay.required", FormError("howOftenPaidThisPayOther", errorRequired))
          .replaceError("", "whenDidYouLastGetPaid.required", FormError("whenDidYouLastGetPaid", errorRequired))
        BadRequest(views.html.your_income.fosteringAllowance(formWithErrorsUpdate))
      },
      fosteringAllowance => claim.update(fosteringAllowance) -> Redirect(controllers.your_income.routes.GDirectPayment.present()))
  }.withPreviewConditionally[YourIncomes](checkGoPreview)

  def fosteringAllowance(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(FosteringAllowance) { implicit claim => Ok(views.html.your_income.fosteringAllowance(form.fill(FosteringAllowance))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    val previousYourIncome = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    if ((previousYourIncome.yourIncome_fostering != yourIncomes.yourIncome_fostering && yourIncomes.yourIncome_fostering.isDefined || !request.flash.isEmpty) && models.domain.YourIncomeFosteringAllowance.visible) c
    else claim -> Redirect(controllers.your_income.routes.GDirectPayment.present())
  }

  private def checkGoPreview(t:(Option[YourIncomes], YourIncomes), c:(Option[Claim],Claim)): Boolean = {
    val currentClaim = c._2
    ReturnToSummaryHelper.haveOtherPaymentsFromFosteringAllowanceChanged(currentClaim)

    //We want to go back to preview from Employment guard questions page if
    // both answers haven't changed or if one hasn't changed and the changed one is 'no' or both answers are no, or
  }
}