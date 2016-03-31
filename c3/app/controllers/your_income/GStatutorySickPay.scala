package controllers.your_income

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain.StatutorySickPay
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import controllers.CarersForms._

/**
  * Created by peterwhitehead on 24/03/2016.
  */
object GStatutorySickPay extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "stillBeingPaidStatutorySickPay" -> carersNonEmptyText.verifying(validYesNo),
    "whenDidYouLastGetPaid" -> optional(dayMonthYear.verifying(validDate)),
    "whoPaidYouStatutorySickPay" -> carersNonEmptyText(maxLength = Mappings.sixty),
    "amountOfStatutorySickPay" -> carersNonEmptyText(maxLength = Mappings.twelve),
    "howOftenPaidStatutorySickPay" -> carersNonEmptyText,
    "howOftenPaidStatutorySickPayOther" -> optional(carersNonEmptyText(maxLength = Mappings.sixty))
  )(StatutorySickPay.apply)(StatutorySickPay.unapply)
    .verifying(StatutorySickPay.whenDidYouLastGetPaidRequired)
    .verifying(StatutorySickPay.howOftenPaidStatutorySickPayItVariesRequired)
  )

  def present = claimingWithCheck {  implicit claim => implicit request => implicit request2lang =>
    track(StatutorySickPay) { implicit claim => Ok(views.html.your_income.statutorySickPay(form.fill(StatutorySickPay))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "statutorySickPay.howOftenPaidStatutorySickPay.required", FormError("howOftenPaidStatutorySickPayOther", errorRequired))
          .replaceError("", "statutorySickPay.whenDidYouLastGetPaid.required", FormError("whenDidYouLastGetPaid", errorRequired))
        BadRequest(views.html.your_income.statutorySickPay(formWithErrorsUpdate))
      },
      statutorySickPay => claim.update(statutorySickPay) -> Redirect(controllers.s_pay_details.routes.GHowWePayYou.present()))
  } withPreview()
}
