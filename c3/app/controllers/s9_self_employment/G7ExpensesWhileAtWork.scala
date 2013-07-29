package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{ChildcareExpensesWhileAtWork, SelfEmploymentPensionsAndExpenses, Claim, ExpensesWhileAtWork}
import models.view.CachedClaim
import utils.helpers.CarersForm._

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

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_self_employment.g7_expensesWhileAtWork(form.fill(ExpensesWhileAtWork), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g7_expensesWhileAtWork(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G8CareProvidersContactDetails.present())
      )
  }
}
