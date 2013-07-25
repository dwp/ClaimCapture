package controllers.s9_pay_details

import play.api.mvc.Controller
import models.view.CachedClaim

object PayDetails extends Controller with CachedClaim {
  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_pay_details.g3_completed(claim.completedQuestionGroups(models.domain.PayDetails)))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.CarersAllowance).firstPage)
  }
}