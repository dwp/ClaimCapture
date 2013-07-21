package controllers.s7_employment

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{Jobs, Employed, Claim}

object Employment extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Employed)

  def jobs(implicit claim: Claim) = claim.questionGroup(Jobs) match {
    case Some(js: Jobs) => js
    case _ => Jobs()
  }

  def completed = claiming { implicit claim => implicit request =>
    Ok("")
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect("")
  }
}