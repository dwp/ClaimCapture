package controllers

import play.api.mvc.{Controller, Action}

trait RedirectController {
  this: Controller =>

  def redirect(url:String) = Action {
    Ok(views.html.common.redirect(url))
  }
}

object RedirectController extends Controller with RedirectController
