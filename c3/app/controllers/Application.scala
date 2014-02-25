package controllers

import app.ConfigProperties._
import play.api.mvc._
import models.view.CachedClaim

object Application extends Controller with CachedClaim {

  val govUk: String = getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance")
  val welshAvailable = getProperty("galluogi.cymraeg", "false")

  def index = Action {
    MovedPermanently(govUk)
  }
}