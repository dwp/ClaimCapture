package controllers

import app.ConfigProperties._
import play.api.mvc._
import models.view.CachedClaim

object Application extends Controller with CachedClaim {

  val govUk: String = getProperty("gov.uk.start.page", "https://www.gov.uk/apply-carers-allowance")

  def index = Action {
    MovedPermanently(govUk)
  }
}