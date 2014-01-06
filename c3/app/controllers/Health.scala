package controllers

import play.api.mvc.{Controller, Action}

trait HealthController {
  this: Controller =>

  def health = Action {
    Ok(views.html.common.health())
  }
}

object HealthController extends Controller with HealthController
