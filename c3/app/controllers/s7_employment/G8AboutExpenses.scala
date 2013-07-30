package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.AboutExpenses
import utils.helpers.CarersForm._
import Employment._

object G8AboutExpenses extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "payForAnythingNecessary" -> nonEmptyText,
      "payAnyoneToLookAfterChildren" -> nonEmptyText,
      "payAnyoneToLookAfterPerson" -> nonEmptyText
    )(AboutExpenses.apply)(AboutExpenses.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    whenSectionVisible(Ok(views.html.s7_employment.g8_aboutExpenses(form.fillWithJobID(AboutExpenses, jobID), completedQuestionGroups(AboutExpenses, jobID))))
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => whenSectionVisible(BadRequest(views.html.s7_employment.g8_aboutExpenses(formWithErrors, completedQuestionGroups(AboutExpenses, jobID)))),
      aboutExpenses => claim.update(jobs.update(aboutExpenses)) -> Redirect(routes.G9NecessaryExpenses.present(jobID)))
  }
}