package controllers

import play.api.mvc.{Controller, Action}

trait RedirectController {
  this: Controller =>

  def redirect(url:String) = Action { request =>
    Ok(views.html.common.redirect(url)(request))
  }
}

object RedirectController extends Controller with RedirectController
