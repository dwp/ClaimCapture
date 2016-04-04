package controllers.your_income

import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{FosteringAllowance, Claim}
import models.view.ClaimHandling._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.{AnyContent, Controller, Request}
import utils.helpers.CarersForm._

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
          .replaceError("", "paymentTypesForThisPay.required", FormError("paymentTypesForThisPay", errorRequired))
          .replaceError("", "howOftenPaidThisPay.required", FormError("howOftenPaidThisPayOther", errorRequired))
          .replaceError("", "whenDidYouLastGetPaid.required", FormError("whenDidYouLastGetPaid", errorRequired))
        BadRequest(views.html.your_income.fosteringAllowance(formWithErrorsUpdate))
      },
      fosteringAllowance => claim.update(fosteringAllowance) -> Redirect(controllers.your_income.routes.GDirectPayment.present()))
  } withPreview()

  def fosteringAllowance(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(FosteringAllowance) { implicit claim => Ok(views.html.your_income.fosteringAllowance(form.fill(FosteringAllowance))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourIncomeFosteringAllowance.visible) c
    else claim -> Redirect(controllers.your_income.routes.GDirectPayment.present())
  }
}