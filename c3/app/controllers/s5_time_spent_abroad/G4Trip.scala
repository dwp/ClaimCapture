package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing

object G4Trip extends Controller with Routing with CachedClaim {
  //override val route = ListMap("TODO" -> routes.G3Trip.fourWeeks/*, "TODO" -> routes.G3Trip.fiftyTwoWeeks*/)
  override val route = "TODO" -> routes.G4Trip.fourWeeks

  def fourWeeks = claiming { implicit claim => implicit request =>
    Ok("")
  }

  /*def fiftyTwoWeeks = claiming { implicit claim => implicit request =>
    Ok("")
  }*/
}