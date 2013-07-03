package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing

object G1NormalResidenceAndCurrentLocation extends Controller with Routing with CachedClaim {

  override val route = "TODO" -> routes.G1NormalResidenceAndCurrentLocation.present

  def present = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation())
  }
}