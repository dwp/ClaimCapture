package controllers

import play.api.mvc.Controller
import models.view.CachedClaim

object ThankYou extends Controller with CachedClaim {
  def present(txnId:String) = claiming { implicit claim => implicit request =>
    Ok(views.html.common.thankYou(txnId))
  }
}