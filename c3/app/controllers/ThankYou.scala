package controllers

import models.view.CachedClaim
import play.api.mvc.Controller

object ThankYou extends Controller with CachedClaim {
  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.common.thankYou())
  }
}