package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AboutExpenses, NecessaryExpenses}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G9NecessaryExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "whatAreThose" -> nonEmptyText,
      "howMuchCostEachWeek" -> nonEmptyText,
      "whyDoYouNeedThose" -> nonEmptyText
    )(NecessaryExpenses.apply)(NecessaryExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    jobs.questionGroup(jobID, AboutExpenses) match {
      case Some(a: AboutExpenses) if a.payForAnythingNecessary == `yes`=>
        dispatch(Ok(views.html.s7_employment.g9_necessaryExpenses(form.fillWithJobID(NecessaryExpenses, jobID), completedQuestionGroups(NecessaryExpenses, jobID))))
      case _ =>
        claim.update(jobs.delete(jobID, NecessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID))
    }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => dispatch(BadRequest(views.html.s7_employment.g9_necessaryExpenses(formWithErrors, completedQuestionGroups(NecessaryExpenses, jobID)))),
      necessaryExpenses => claim.update(jobs.update(necessaryExpenses)) -> Redirect(routes.G10ChildcareExpenses.present(jobID)))
  }
}