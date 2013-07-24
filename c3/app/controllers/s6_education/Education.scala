package controllers.s6_education

import play.api.mvc._
import models.view.CachedClaim
import models.domain.Claim
import play.api.templates.Html

object Education extends Controller with CachedClaim {
  def whenVisible(claim: Claim)(f: => SimpleResult[Html]) = {
    if (claim.isSectionVisible(models.domain.Education)) f
    else Redirect(controllers.s7_employment.routes.G1BeenEmployed.present())
  }

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.Education)

  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s6_education.g3_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.Education).firstPage)
  }
}