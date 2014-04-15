package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.{AboutExpenses, NecessaryExpenses}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import controllers.CarersForms._

object G9NecessaryExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "jobTitle" -> carersNonEmptyText,
    "whatAreThose" -> carersNonEmptyText
  )(NecessaryExpenses.apply)(NecessaryExpenses.unapply))

  def present(jobID: String) = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payForAnythingNecessary == `yes`=>
        track(NecessaryExpenses) { implicit claim => Ok(views.html.s7_employment.g9_necessaryExpenses(form.fillWithJobID(NecessaryExpenses, jobID))) }
      case _ =>
        val updatedClaim = claim.update(jobs.delete(jobID, NecessaryExpenses))
        updatedClaim.update(jobs.delete(jobID, NecessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID))
    }
  }

  def submit = claimingWithCheckInJob { jobID => implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val pastPresentLabelWhatAreThose = labelForEmployment(claim, lang, "whatAreThose", jobID)
        val pastPresentLabelJobTitle = labelForEmployment(claim, lang, "jobTitle", jobID)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("jobTitle", "error.required", FormError("jobTitle", "error.required", Seq(pastPresentLabelJobTitle)))
          .replaceError("jobTitle", "error.restricted.characters", FormError("jobTitle", "error.restricted.characters", Seq(pastPresentLabelJobTitle)))
          .replaceError("whatAreThose", "error.required", FormError("whatAreThose", "error.required", Seq(pastPresentLabelWhatAreThose)))
          .replaceError("whatAreThose", "error.restricted.characters", FormError("whatAreThose", "error.restricted.characters", Seq(pastPresentLabelWhatAreThose)))
        BadRequest(views.html.s7_employment.g9_necessaryExpenses(formWithErrorsUpdate))
      },
      necessaryExpenses => claim.update(jobs.update(necessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID)))
  }
}