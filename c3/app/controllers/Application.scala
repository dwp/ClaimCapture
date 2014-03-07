package controllers

import app.ConfigProperties._
import play.api.mvc._
import services.EmailServices

object Application extends Controller {
  def index = Action {
    MovedPermanently(getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance"))
  }

  def mail = Action {
    import EmailServices._
    sendEmail to "khanser@gmail.com" withSubject "Another test email" withBody "O HAI"
    Ok("")
  }
}