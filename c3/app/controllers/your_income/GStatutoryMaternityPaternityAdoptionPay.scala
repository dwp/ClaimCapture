package controllers.your_income

import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{StatutoryMaternityPaternityAdoptionPay, Claim, StatutorySickPay}
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
object GStatutoryMaternityPaternityAdoptionPay extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "paymentTypesForThisPay" -> carersNonEmptyText.verifying(validPaymentType),
    "stillBeingPaidThisPay" -> carersNonEmptyText.verifying(validYesNo),
    "whenDidYouLastGetPaid" -> optional(dayMonthYear.verifying(validDate)),
    "whoPaidYouThisPay" -> carersNonEmptyText(maxLength = Mappings.sixty),
    "amountOfThisPay" -> carersNonEmptyText(maxLength = Mappings.twelve),
    "howOftenPaidThisPay" -> carersNonEmptyText.verifying(validPaymentFrequency),
    "howOftenPaidThisPayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty))
  )(StatutoryMaternityPaternityAdoptionPay.apply)(StatutoryMaternityPaternityAdoptionPay.unapply)
    .verifying(StatutoryMaternityPaternityAdoptionPay.whenDidYouLastGetPaidRequired)
    .verifying(StatutoryMaternityPaternityAdoptionPay.howOftenPaidThisPayItVariesRequired)
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
      statutoryMaternityPaternityAdoptionPay => claim.update(statutoryMaternityPaternityAdoptionPay) -> Redirect(controllers.s_pay_details.routes.GHowWePayYou.present()))
  } withPreview()

  def statutoryMaternityPaternityPay(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(StatutoryMaternityPaternityAdoptionPay) { implicit claim => Ok(views.html.your_income.statutoryMaternityPaternityAdoptionPay(form.fill(StatutoryMaternityPaternityAdoptionPay))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourIncomeStatutoryMaternityPaternityAdoptionPay.visible) c
    else claim -> Redirect(controllers.s_pay_details.routes.GHowWePayYou.present())
  }
}