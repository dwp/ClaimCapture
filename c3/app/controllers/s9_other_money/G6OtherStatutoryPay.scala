package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.OtherStatutoryPay
import utils.helpers.CarersForm._
import models.view.Navigable
import controllers.CarersForms._
import play.api.data.FormError

object G6OtherStatutoryPay extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "otherPay" -> nonEmptyText.verifying(validYesNo),
    "howMuch" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
    "employersName" -> optional(carersNonEmptyText(maxLength = sixty)),
    "employersAddress" -> optional(address),
    "employersPostcode" -> optional(text verifying validPostcode)
  )(OtherStatutoryPay.apply)(OtherStatutoryPay.unapply)
    .verifying("employersName.required", validateEmployerName _))

  def validateEmployerName(otherStatutoryPay: OtherStatutoryPay) = {
    otherStatutoryPay.otherPay match {
      case `yes` => otherStatutoryPay.employersName.isDefined
      case _ => true
    }
  }

  def present = claiming { implicit claim => implicit request =>
    track(OtherStatutoryPay) { implicit claim => Ok(views.html.s9_other_money.g6_otherStatutoryPay(form.fill(OtherStatutoryPay)))}
  }

  def submit = claiming {implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "employersName.required", FormError("employersName", "error.required"))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength"))
          .replaceError("otherPay","error.required", FormError("otherPay", "error.required",Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          .replaceError("employersAddress.lineOne", FormError("employersAddress", "error.restricted.characters"))
          .replaceError("employersAddress.lineTwo",  FormError("employersAddress", "error.restricted.characters"))
          .replaceError("employersAddress.lineThree", FormError("employersAddress", "error.restricted.characters"))
        BadRequest(views.html.s9_other_money.g6_otherStatutoryPay(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.OtherMoney.completed()))
  }
}