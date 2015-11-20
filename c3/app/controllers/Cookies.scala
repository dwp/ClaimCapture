package controllers

import play.api.Play._
import play.api.i18n._
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

object Cookies extends Controller with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def page(languageCode: String) = Action { request =>
    val lang = Lang(languageCode)
    Ok(views.html.cookies.cookies(messagesApi)(lang, request, request.flash, applicationMessages))
  }

}
