package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ChildcareExpenses, NecessaryExpenses}
import utils.helpers.CarersForm._
import Employment._

object G10ChildcareExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "howMuchCostChildcare" -> optional(text),
      "whoLooksAfterChildren" -> nonEmptyText,
      "relationToYou" -> optional(text),
      "relationToPartner" -> optional(text)
    )(ChildcareExpenses.apply)(ChildcareExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g10_childcareExpenses(form.fillWithJobID(ChildcareExpenses, jobID), completedQuestionGroups(ChildcareExpenses, jobID)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g10_childcareExpenses(formWithErrors, completedQuestionGroups(ChildcareExpenses, formWithErrors("jobID").value.get))),
      childcareExpenses => claim.update(jobs.update(childcareExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(childcareExpenses.jobID)))
  }
}