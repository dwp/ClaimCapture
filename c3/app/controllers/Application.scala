package controllers

import app.ConfigProperties._
import play.api.Play._
import play.api.mvc._
import play.api.i18n._


object Application extends Controller with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def index = Action {
    MovedPermanently(getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance"))
  }

  def backButtonPage = Action { implicit request =>
    Ok(views.html.common.backButton(messagesApi))
  }

  def backButtonCircsPage = Action { implicit request =>
    Ok(views.html.common.backButton(messagesApi))
  }
}
