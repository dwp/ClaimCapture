package controllers

import app.ConfigProperties._
import play.api.mvc.Controller
import models.view.CachedClaim

object ClaimEnding extends Controller with CachedClaim {
  val startUrl: String = getProperty("claim.start.page", "/allowance/benefits")

  def timeout = ending {
    Ok(views.html.common.session_timeout(startUrl))
  }

  def error = ending {
    Ok(views.html.common.error(startUrl))
  }

  def thankyou = ending {
    Ok(views.html.common.thankYouClaim())
  }

}
