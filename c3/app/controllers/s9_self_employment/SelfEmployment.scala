package controllers.s9_self_employment

import play.api.mvc._
import models.view._

object SelfEmployment extends Controller with CachedClaim {


  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_self_employment.g9_completed(claim.completedQuestionGroups(models.domain.SelfEmployment)))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.CarersAllowance).firstPage)
  }
}