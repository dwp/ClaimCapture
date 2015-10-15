package controllers

import play.api.i18n.Lang
import play.api.mvc.{Action, Controller}

object Cookies extends Controller {

  def page(languageCode: String) = Action { request =>
    val lang = Lang(languageCode)
    Ok(views.html.cookies.cookies(lang, request,request.flash))
  }

}
