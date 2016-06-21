package controllers

import play.api.i18n._
import play.api.mvc.{Action, Controller}
import play.api.Play.current

object Cookies extends Controller with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def page(languageCode: String) = Action { implicit request =>
    implicit val isCookiePage = true
    Ok(views.html.cookies.cookies())
  }

  def cookiesTablePage = Action { implicit request =>
    Ok(views.html.cookies.cookiesTable())
  }
}
