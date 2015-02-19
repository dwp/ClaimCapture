package controllers

import app.ConfigProperties._
import play.api.mvc._

object Application extends Controller {
  def index = Action {
    MovedPermanently(getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance"))
  }

  def backButtonPage = Action { implicit request =>
    Ok(views.html.common.backButton())
  }
}