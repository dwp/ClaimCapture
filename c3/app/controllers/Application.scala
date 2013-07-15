package controllers

import play.api.mvc._

object Application extends Controller {
  def index = Action {
    Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
  }

  def timeout = Action {
    Ok(views.html.common.session_timeout())
  }
}