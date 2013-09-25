package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current

object Application extends Controller {
  def index = Action {
    Redirect("/allowance/benefits")
  }

  def timeout = Action {
    Ok(views.html.common.session_timeout())
  }

  def circsTimeout = Action {
    Ok(views.html.common.session_timeout("/circumstances/identification/about-you"))
  }

  def error(key:String) = Action { request =>
    // Clear the cache to ensure no duplicate submission
    val cacheKey = request.session.get(key).orNull
    Cache.set(cacheKey, None)
    Ok(views.html.common.error())
  }
}