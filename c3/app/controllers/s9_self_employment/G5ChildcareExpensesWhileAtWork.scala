package controllers.s9_self_employment

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.s9_self_employment.SelfEmployment.whenSectionVisible

object G5ChildcareExpensesWhileAtWork extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ExpensesWhileAtWork)

  val form = Form(
    mapping(
      call(routes.G5ChildcareExpensesWhileAtWork.present()),
      "howMuchYouPay" -> optional(text(maxLength = sixty)),
      "whoLooksAfterChildren" -> nonEmptyText(maxLength = sixty),
      "whatRelationIsToYou" -> optional(text(maxLength = sixty)),
      "relationToPartner" -> optional(text(maxLength = sixty)),
      "whatRelationIsTothePersonYouCareFor" -> optional(text(maxLength = sixty))
    )(ChildcareExpensesWhileAtWork.apply)(ChildcareExpensesWhileAtWork.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s9_self_employment.g5_childcareExpensesWhileAtWork(form.fill(ChildcareExpensesWhileAtWork), completedQuestionGroups)))

  }

  def submit = claiming { implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g5_childcareExpensesWhileAtWork(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G6ChildcareProvidersContactDetails.present())
      )
  }
}
