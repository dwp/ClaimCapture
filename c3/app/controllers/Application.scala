package controllers

import app.ConfigProperties._
import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import models.view.CachedClaim

object Application extends Controller with CachedClaim {
  val startUrl: String = getProperty("claim.start.page", "/allowance/benefits")

  def index = Action {
    Redirect(startUrl)
  }
}