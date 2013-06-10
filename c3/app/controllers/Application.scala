package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  def index = Action {
    Redirect(routes.CarersAllowance.benefits())
  }

  def cookies = Action { request =>
    request.session.get("connected").map { key =>
      Logger.debug("Session Key from cookie.")

      Ok(views.html.index("Key : " + key))
    }.getOrElse {
      val key = java.util.UUID.randomUUID().toString
      Logger.debug("Generating new Session Key: " + key)

      Ok(views.html.index("New Session Key:" + key)).withSession("connected" -> key)
    }
  }
}