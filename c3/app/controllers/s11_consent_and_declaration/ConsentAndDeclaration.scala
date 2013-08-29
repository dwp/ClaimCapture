package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import models.domain._
import models.view.Navigable

object ConsentAndDeclaration extends Controller with CachedClaim with Navigable {
  def completed = claiming { implicit claim => implicit request =>
    track(models.domain.ConsentAndDeclaration) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit())}
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }
}