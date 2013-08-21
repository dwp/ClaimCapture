package controllers.s7_employment

import language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import models.domain.JobCompletion

object G14JobCompletion extends Controller with CachedClaim with Navigable {
  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(JobCompletion) { implicit claim => Ok(views.html.s7_employment.g14_jobCompletion(jobID)) }
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(routes.G1BeenEmployed.present())
  }
}