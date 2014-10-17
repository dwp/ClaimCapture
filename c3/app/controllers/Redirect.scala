package controllers

import play.api.mvc.{Controller, Action}
import play.api.i18n.Lang
import utils.helpers.CarersLanguageHelper

trait RedirectController extends CarersLanguageHelper {
  this: Controller =>

  def redirect(url:String) = Action { request =>
    Ok(views.html.common.redirect(url)(request,lang(request)))
  }
}

object RedirectController extends Controller with RedirectController
