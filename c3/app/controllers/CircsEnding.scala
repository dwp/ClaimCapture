package controllers

import play.api.mvc.Controller
import models.view.CachedChangeOfCircs

object CircsEnding extends Controller with CachedChangeOfCircs {

  def timeout = ending {
    Ok(views.html.common.session_timeout(startPage))
  }

  def error = ending {
    Ok(views.html.common.error(startPage))
  }

  def thankyou = ending {
    Ok(views.html.common.thankYouCircs())
  }

}
