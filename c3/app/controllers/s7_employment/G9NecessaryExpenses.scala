package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, NecessaryExpenses}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError

object G9NecessaryExpenses extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "whatAreThose" -> nonEmptyText,
    "howMuchCostEachWeek" -> nonEmptyText,
    "whyDoYouNeedThose" -> nonEmptyText
  )(NecessaryExpenses.apply)(NecessaryExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payForAnythingNecessary == `yes`=>
        track() { implicit claim => Ok(views.html.s7_employment.g9_necessaryExpenses(form.fillWithJobID(NecessaryExpenses, jobID))) }
      case _ =>
        claim.update(jobs.delete(jobID, NecessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("whatAreThose", "error.required", FormError("whatAreThose", "error.required", Seq(pastPresentLabelForEmployment(claim, wereYou.toLowerCase.take(4), areYou.toLowerCase.take(3) , jobID))))
          .replaceError("howMuchCostEachWeek", "error.required", FormError("howMuchCostEachWeek", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase.take(3), doYou.toLowerCase.take(2) , jobID))))
          .replaceError("whyDoYouNeedThose", "error.required", FormError("whyDoYouNeedThose", "error.required", Seq(pastPresentLabelForEmployment(claim, didYou.toLowerCase, doYou.toLowerCase , jobID))))
        BadRequest(views.html.s7_employment.g9_necessaryExpenses(formWithErrorsUpdate))
      },
      necessaryExpenses => claim.update(jobs.update(necessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID)))
  }
}