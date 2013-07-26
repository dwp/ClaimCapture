package controllers.s7_employment

import language.reflectiveCalls
import models.view.CachedClaim
import play.api.mvc.Controller
import models.domain.JobCompletion
import Employment._

object G14JobCompletion extends Controller with CachedClaim {
  def present(jobID: String) = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g14_jobCompletion(completedQuestionGroups(JobCompletion, jobID)))
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(routes.Employment.completed())
  }
}