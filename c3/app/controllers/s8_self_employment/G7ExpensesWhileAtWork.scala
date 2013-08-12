package controllers.s8_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, ExpensesWhileAtWork}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s8_self_employment.SelfEmployment.whenSectionVisible
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import scala.Some

object G7ExpensesWhileAtWork extends Controller with SelfEmploymentRouting with CachedClaim {
  val form = Form(
    mapping(
      "howMuchYouPay" -> nonEmptyText(maxLength = 8).verifying(validDecimalNumber),
      "nameOfPerson" -> nonEmptyText(maxLength = sixty),
      "whatRelationIsToYou" -> nonEmptyText(maxLength = sixty),
      "whatRelationIsTothePersonYouCareFor" -> nonEmptyText(maxLength = sixty)
    )(ExpensesWhileAtWork.apply)(ExpensesWhileAtWork.unapply))

  def present = claiming { implicit claim => implicit request =>
    val payToLookPersonYouCareFor = claim.questionGroup(SelfEmploymentPensionsAndExpenses) match {
      case Some(s: SelfEmploymentPensionsAndExpenses) => s.didYouPayToLookAfterThePersonYouCaredFor == `yes`
      case _ => false
    }

    payToLookPersonYouCareFor match {
      case true => whenSectionVisible(Ok(views.html.s8_self_employment.g7_expensesWhileAtWork(form.fill(ExpensesWhileAtWork), completedQuestionGroups(ExpensesWhileAtWork))))
      case false => claim.delete(ExpensesWhileAtWork) -> Redirect(routes.G8CareProvidersContactDetails.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howMuchYouPay", "error.required", FormError("howMuchYouPay", "error.required", Seq(didYouDoYouIfSelfEmployed.toLowerCase)))
          .replaceError("howMuchYouPay", "decimal.invalid", FormError("howMuchYouPay", "decimal.invalid", Seq(didYouDoYouIfSelfEmployed.toLowerCase)))
        BadRequest(views.html.s8_self_employment.g7_expensesWhileAtWork(formWithErrorsUpdate, completedQuestionGroups(ExpensesWhileAtWork)))
      },
      f => claim.update(f) -> Redirect(routes.G8CareProvidersContactDetails.present())
    )
  }
}