package controllers

import play.api.mvc._
import models.view.CachedDigitalForm

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

  def error = Action {
    Ok(views.html.common.error())
  }
}