package utils.helpers

import play.api.mvc.RequestHeader
import play.api.Play
import play.api.i18n.Lang

trait CarersLanguageHelper {
  implicit def lang(implicit request: RequestHeader, messages: play.api.i18n.MessagesApi) = {
    play.api.Play.maybeApplication.map { implicit app =>
      val maybeLangFromCookie = request.cookies.get(Play.langCookieName).flatMap(c => Lang.get(c.value))
      maybeLangFromCookie.getOrElse(play.api.i18n.Lang.preferred(request.acceptLanguages))
    }.getOrElse(request.acceptLanguages.headOption.getOrElse(play.api.i18n.Lang.defaultLang))
  }
}
