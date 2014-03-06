package controllers

import play.api.mvc.{Controller, Action}
import play.api.i18n.Lang

trait RedirectController {
  this: Controller =>

  def redirect(url:String) = Action { request =>
    Ok(views.html.common.redirect(url)(lang(request), request))
  }
}

object RedirectController extends Controller with RedirectController
