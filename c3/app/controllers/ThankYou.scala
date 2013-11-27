package controllers

import play.api.mvc.{Action, Controller}
import play.api.Logger

object ThankYou extends Controller {
  def claim = Action { request =>
    Ok(views.html.common.thankYouClaim())
  }
  def circs = Action { request =>
    Ok(views.html.common.thankYouCircs())
  }
}