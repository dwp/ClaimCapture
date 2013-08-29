package controllers

import play.api.mvc._
import models.view.CachedClaim

object Application extends Controller with CachedClaim {
  def index = Action {
    Redirect("/allowance/benefits")
  }

  def timeout = Action {
    Ok(views.html.common.session_timeout())
  }

  def error = Action {
    Ok(views.html.common.error())
  }
}