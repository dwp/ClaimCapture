package controllers.s11_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable

object ConsentAndDeclaration extends Controller with CachedClaim with Navigable {
  def completed = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(models.domain.ConsentAndDeclaration) { implicit claim => Ok(views.html.s11_consent_and_declaration.g5_submit())}
  }

  def completedSubmit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Redirect("/allowance/benefits")
  }
}