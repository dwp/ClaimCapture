package controllers

import play.api.mvc.{Action, Controller}
import play.api.Logger

object ThankYou extends Controller {
  def claim(txnId:String) = Action { request =>
    Logger.debug(txnId)
    Ok(views.html.common.thankYouClaim(txnId))
  }
  def circs(txnId:String) = Action { request =>
    Ok(views.html.common.thankYouCircs(txnId))
  }
}