package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{CachedClaim, Navigable}
import models.domain.StatutorySickPay
import controllers.Mappings._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.data.FormError

object G5StatutorySickPay extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "haveYouHadAnyStatutorySickPay" ->  nonEmptyText.verifying(validYesNo),
    "howMuch" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
    "employersName" -> optional(carersNonEmptyText(maxLength = sixty)),
    "employersAddress" -> optional(address),
    "employersPostcode" -> optional(text verifying validPostcode)
  )(StatutorySickPay.apply)(StatutorySickPay.unapply)
    .verifying("employersName.required", validateEmployerName _)
    .verifying("employersHowMuch.required", validateHowMuch _)
  )

  def validateEmployerName(statutorySickPay: StatutorySickPay) = {
    statutorySickPay.haveYouHadAnyStatutorySickPay match {
      case `yes` => statutorySickPay.employersName.isDefined
      case _ => true
    }
  }

  def validateHowMuch(statutorySickPay: StatutorySickPay) = {
    statutorySickPay.haveYouHadAnyStatutorySickPay match {
      case `yes` => statutorySickPay.howMuch.isDefined
      case _ => true
    }
  }

  def present = claiming { implicit claim => implicit request =>
    track(StatutorySickPay) { implicit claim => Ok(views.html.s9_other_money.g5_statutorySickPay(form.fill(StatutorySickPay))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "employersName.required", FormError("employersName", "error.required"))
          .replaceError("", "employersHowMuch.required", FormError("howMuch", "error.required"))
        BadRequest(views.html.s9_other_money.g5_statutorySickPay(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(routes.G6OtherStatutoryPay.present()))
  }
}