package controllers

import play.api.mvc.Controller
import models.view.CachedChangeOfCircs

object CircsEnding extends Controller with CachedChangeOfCircs {

  def timeout = endingOnError {implicit claim =>  implicit request =>  lang =>
    Ok(views.html.common.session_timeout(startPage))
  }

  def error = endingOnError {implicit claim =>  implicit request =>  lang =>
    Ok(views.html.common.error(startPage))
  }

  def thankyou = ending {implicit claim =>  implicit request =>  lang =>
    Ok(views.html.common.thankYouCircs(lang))
  }

}
