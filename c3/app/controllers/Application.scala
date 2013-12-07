package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import models.view.CachedClaim

object Application extends Controller with CachedClaim {
  val circsStartUrl: String = "/circumstances/identification/about-you"

  val claimStartUrl: String = "/allowance/benefits"

  def index = Action {
    Redirect(claimStartUrl)
  }

  def timeout = ending {
    Ok(views.html.common.session_timeout(claimStartUrl))
  }

  def circsTimeout = ending {
    Ok(views.html.common.session_timeout(circsStartUrl))
  }

  def error = ending {
    Ok(views.html.common.error(claimStartUrl))
  }

  def circsError = ending {
    Ok(views.html.common.error(circsStartUrl))
  }
}