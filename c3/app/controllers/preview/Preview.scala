package controllers.preview

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import play.api.Logger
import models.domain.{Benefits, YourDetails}

object Preview extends Controller with CachedClaim with Navigable {


  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(models.domain.Preview) { implicit claim => Ok(views.html.preview.preview()) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Redirect(controllers.s11_consent_and_declaration.routes.G1AdditionalInfo.present)
  }
}
