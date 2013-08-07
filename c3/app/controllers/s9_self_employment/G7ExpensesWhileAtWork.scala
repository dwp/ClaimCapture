package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{SelfEmploymentPensionsAndExpenses, Claim, ExpensesWhileAtWork}
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s9_self_employment.SelfEmployment.whenSectionVisible
import scala.Some

object G7ExpensesWhileAtWork extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ExpensesWhileAtWork)

  val form = Form(
    mapping(
      call(routes.G7ExpensesWhileAtWork.present()),
      "howMuchYouPay" -> optional(text(maxLength = sixty)),
      "nameOfPerson" -> nonEmptyText(maxLength = sixty),
      "whatRelationIsToYou" -> optional(text(maxLength = sixty)),
      "whatRelationIsTothePersonYouCareFor" -> optional(text(maxLength = sixty))
    )(ExpensesWhileAtWork.apply)(ExpensesWhileAtWork.unapply)
  )

  def present = claiming {
    implicit claim => implicit request =>

      val payToLookPersonYouCareFor = claim.questionGroup(SelfEmploymentPensionsAndExpenses) match {
        case Some(s: SelfEmploymentPensionsAndExpenses) => s.didYouPayToLookAfterThePersonYouCaredFor == `yes`
        case _ => false
      }

      payToLookPersonYouCareFor match {
        case true => whenSectionVisible(Ok(views.html.s9_self_employment.g7_expensesWhileAtWork(form.fill(ExpensesWhileAtWork), completedQuestionGroups)))
        case false => claim.delete(ExpensesWhileAtWork) -> Redirect(routes.G8CareProvidersContactDetails.present())
      }
  }

  def submit = claiming {
    implicit claim =>
      implicit request =>
        form.bindEncrypted.fold(
          formWithErrors => BadRequest(views.html.s9_self_employment.g7_expensesWhileAtWork(formWithErrors, completedQuestionGroups)),
          f => claim.update(f) -> Redirect(routes.G8CareProvidersContactDetails.present())
        )
  }
}
