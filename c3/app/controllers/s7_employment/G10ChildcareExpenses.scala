package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, ChildcareExpenses}
import utils.helpers.CarersForm._
import controllers.Mappings._
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
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payAnyoneToLookAfterChildren == `yes`=> Ok(views.html.s7_employment.g10_childcareExpenses(form.fillWithJobID(ChildcareExpenses, jobID), completedQuestionGroups(ChildcareExpenses, jobID)))
      case _ => claim.update(jobs.delete(jobID, ChildcareExpenses)) -> Redirect(routes.G12PersonYouCareForExpenses.present(jobID))
    }

  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g10_childcareExpenses(formWithErrors, completedQuestionGroups(ChildcareExpenses, jobID))),
      childcareExpenses => claim.update(jobs.update(childcareExpenses)) -> Redirect(routes.G11ChildcareProvider.present(jobID)))
  }
}