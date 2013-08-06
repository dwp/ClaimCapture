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
import controllers.s7_employment.Employment._
import scala.Some


object G5ChildcareExpensesWhileAtWork extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ChildcareExpensesWhileAtWork)

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

  def present = claiming {
    implicit claim => implicit request =>
      val yesNoList = jobs.map(j => j.apply(AboutExpenses) match {
        case Some(n: AboutExpenses) => n.payAnyoneToLookAfterChildren
        case _ => "no"
      })

      yesNoList.count(_ == "yes") > 0 match {
        case true => whenSectionVisible(Ok(views.html.s9_self_employment.g5_childcareExpensesWhileAtWork(form.fill(ChildcareExpensesWhileAtWork), completedQuestionGroups)))
        case false => claim.delete(ChildcareExpensesWhileAtWork) -> Redirect(routes.G6ChildcareProvidersContactDetails.present())
      }
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s9_self_employment.g5_childcareExpensesWhileAtWork(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G6ChildcareProvidersContactDetails.present())
      )
  }
}
