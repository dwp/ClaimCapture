package controllers

import play.api.mvc.Controller
import models.view.CachedChangeOfCircs

object CircsEnding extends Controller with CachedChangeOfCircs {

  def timeout = ending { implicit claim => implicit request  => implicit lang =>
    Ok(views.html.common.session_timeout(startPage))
  }

  def error = ending { implicit claim => implicit request  => implicit lang =>
    Ok(views.html.common.error(startPage))
  }

  def thankyou = ending { implicit claim => implicit request  => implicit lang =>
    Ok(views.html.common.thankYouCircs())
  }

}
