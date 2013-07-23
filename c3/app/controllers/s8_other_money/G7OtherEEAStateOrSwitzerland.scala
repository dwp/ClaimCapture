package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{OtherMoneyOtherEEAStateOrSwitzerland, Claim}
import utils.helpers.CarersForm._

object G7OtherEEAStateOrSwitzerland extends Controller with CachedClaim {

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherMoneyOtherEEAStateOrSwitzerland)

  val form = Form(
    mapping(
      "receivingPensionFromAnotherEEA" -> nonEmptyText.verifying(validYesNo),
      "payingInsuranceToAnotherEEA" -> nonEmptyText.verifying(validYesNo),
      call(routes.G6OtherStatutoryPay.present())
    )(OtherMoneyOtherEEAStateOrSwitzerland.apply)(OtherMoneyOtherEEAStateOrSwitzerland.unapply))

  def present = claiming {
    implicit claim =>
      implicit request =>
        /*val currentForm: Form[OtherStatutoryPay] = claim.questionGroup(OtherStatutoryPay) match {
          case Some(t: OtherStatutoryPay) => form.fill(t)
          case _ => form
        }
        Ok(views.html.s8_other_money.g6_otherStatutoryPay(currentForm, completedQuestionGroups))*/
        Ok(<p>Hello</p>)
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        /*form.bindEncrypted.fold(
          formWithErrors => {
            val formWithErrorsUpdate = formWithErrors.replaceError("", "employersName.required", FormError("employersName", "error.required"))
            BadRequest(views.html.s8_other_money.g6_otherStatutoryPay(formWithErrorsUpdate, completedQuestionGroups))
          },
          f => claim.update(f) -> Redirect(routes.OtherMoney.completed())
        )*/
        Ok(<p>Hello</p>)
  }

}
