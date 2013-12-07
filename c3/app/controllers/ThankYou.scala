package controllers

import play.api.mvc.{Action, Controller}
import play.api.Logger
import models.view.CachedClaim

object ThankYou extends Controller with CachedClaim {
  def claim = ending {
    Ok(views.html.common.thankYouClaim())
  }
  def circs = ending {
    Ok(views.html.common.thankYouCircs())
  }
}