package controllers

import play.api.mvc._
import models.view.CachedClaim
import play.api.cache.Cache
import play.api.Play.current

object Application extends Controller with CachedClaim {
  def index = Action {
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }

  def timeout = Action {
    Ok(views.html.common.session_timeout())
  }

  def error = Action {
    request =>
    // Clear the cache to ensure no duplicate submission
      val key = request.session.get(CachedClaim.claimKey).orNull
      Cache.set(key, None)
      Ok(views.html.common.error())
  }
}