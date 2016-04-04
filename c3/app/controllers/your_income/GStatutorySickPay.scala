package controllers.your_income

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.{Claim, StatutorySickPay}
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
  } withPreview()

  def statutorySickPay(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(StatutorySickPay) { implicit claim => Ok(views.html.your_income.statutorySickPay(form.fill(StatutorySickPay))) }
  }

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourIncomeStatutorySickPay.visible) c
    else claim -> Redirect(controllers.your_income.routes.GStatutoryMaternityPaternityAdoptionPay.present())
  }
}