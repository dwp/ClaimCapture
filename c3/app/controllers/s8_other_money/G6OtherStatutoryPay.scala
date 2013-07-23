package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{OtherStatutoryPay, Claim}
import utils.helpers.CarersForm._

object G6OtherStatutoryPay extends Controller with CachedClaim {

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherStatutoryPay)

  val form = Form(
    mapping(
      "otherPay" -> nonEmptyText.verifying(validYesNo),
      "howMuch" -> optional(text(maxLength = sixty)),
      "howOften" -> optional(paymentFrequency),
      "employersName" -> optional(nonEmptyText(maxLength = sixty)),
      "employersAddress" -> optional(address),
      "employersPostcode" -> optional(text verifying validPostcode),
      call(routes.G6OtherStatutoryPay.present())
    )(OtherStatutoryPay.apply)(OtherStatutoryPay.unapply)
      .verifying("employersName.required", validateEmployerName _))

  def validateEmployerName(otherStatutoryPay:OtherStatutoryPay) = {
    otherStatutoryPay.otherPay match {
      case `yes` => otherStatutoryPay.employersName.isDefined
      case _ => true
    }
  }

  def present = claiming {
    implicit claim =>
      implicit request =>
        val currentForm: Form[OtherStatutoryPay] = claim.questionGroup(OtherStatutoryPay) match {
          case Some(t: OtherStatutoryPay) => form.fill(t)
          case _ => form
        }
        Ok(views.html.s8_other_money.g6_otherStatutoryPay(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => {
            val formWithErrorsUpdate = formWithErrors.replaceError("","employersName.required", FormError("employersName", "error.required"))
            BadRequest(views.html.s8_other_money.g6_otherStatutoryPay(formWithErrorsUpdate, completedQuestionGroups))
          },
          f => claim.update(f) -> Redirect(routes.OtherMoney.completed())
        )
  }

}
