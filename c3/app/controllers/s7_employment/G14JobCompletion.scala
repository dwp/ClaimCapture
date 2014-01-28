package controllers.s7_employment

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import models.domain.{BeenEmployed, JobCompletion}
import controllers.s7_employment.Employment._

object G14JobCompletion extends Controller with CachedClaim with Navigable {
  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    claim.update(BeenEmployed(beenEmployed="")).update(jobs.completeJob(jobID)) -> Redirect(routes.G1BeenEmployed.present())
  }
}