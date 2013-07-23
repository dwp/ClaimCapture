package controllers.s10_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim

object ConsentAndDeclaration extends Controller with CachedClaim {
  def completed = claiming { implicit claim => implicit request =>
    Ok(views.html.s6_pay_details.g3_completed(claim.completedQuestionGroups(models.domain.PayDetails)))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }
}