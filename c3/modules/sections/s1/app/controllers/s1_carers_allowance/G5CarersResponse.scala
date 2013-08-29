package controllers.s1_carers_allowance

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable

object G5CarersResponse extends Controller with CachedClaim with Navigable {
  def present = claiming { implicit claim => implicit request =>
    track(models.domain.AboutYou) { implicit claim => Ok(views.html.s1_carers_allowance.g5_carersResponse()) }
  }

  def submit = claiming { implicit claim => implicit request =>
    Redirect(claim.nextSection(models.domain.CarersAllowance).firstPage)
  }
}