package controllers.s9_self_employment

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain.{Claim, ExpensesWhileAtWork}
import models.view.CachedClaim
import utils.helpers.CarersForm._

object G7ExpensesWhileAtWork extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ExpensesWhileAtWork)

  val form = Form(
    mapping(
      call(routes.G7ExpensesWhileAtWork.present()),
      "howMuchYouPay" -> optional(text),
      "nameOfPerson" -> nonEmptyText,
      "whatRelationIsToYou" -> optional(text),
      "whatRelationIsTothePersonYouCareFor" -> optional(text)
    )(ExpensesWhileAtWork.apply)(ExpensesWhileAtWork.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    //Ok(views.html.s9_self_employment.g1_aboutSelfEmployment(form, completedQuestionGroups))
    Ok(<p>Hello, World!</p>)
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g7_expensesWhileAtWork(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G8CareProvidersContactDetails.present())
      )
  }
}
