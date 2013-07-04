package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing

object G2AbroadForMoreThan4Weeks extends Controller with Routing with CachedClaim {
  override val route = "TODO" -> routes.G2AbroadForMoreThan4Weeks.present

  def present = claiming { implicit claim => implicit request =>
    Ok("")
  }
}