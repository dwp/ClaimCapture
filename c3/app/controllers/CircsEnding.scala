package controllers

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, CachedClaim}

object CircsEnding extends Controller with CachedChangeOfCircs {
  val startUrl: String = "/circumstances/identification/about-you"

  def timeout = ending {
    Ok(views.html.common.session_timeout(startUrl))
  }

  def error = ending {
    Ok(views.html.common.error(startUrl))
  }

  def thankyou = ending {
    Ok(views.html.common.thankYouCircs())
  }

}
