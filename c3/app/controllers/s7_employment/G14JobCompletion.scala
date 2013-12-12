package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import models.domain.{BeenEmployed, JobCompletion}
import controllers.s7_employment.Employment._

object G14JobCompletion extends Controller with CachedClaim with Navigable {
  def present(jobID: String) = claiming { implicit claim => implicit request => implicit lang =>
    track(JobCompletion) { implicit claim => Ok(views.html.s7_employment.g14_jobCompletion(jobID)) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request => implicit lang =>
    claim.update(BeenEmployed(beenEmployed="")).update(jobs.completeJob(jobID)) -> Redirect(routes.G1BeenEmployed.present())
  }
}