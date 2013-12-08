package controllers

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import models.view.CachedClaim

object Application extends Controller with CachedClaim {
  val claimStartUrl: String = "/allowance/benefits"

  def index = Action {
    Redirect(claimStartUrl)
  }
}