package controllers

import play.api.mvc.Controller
import models.view.CachedClaim
import app.ConfigProperties._
import services.EmailServices
import models.domain.PreviewModel

object ClaimEnding extends Controller with CachedClaim {

  def timeout = endingOnError {implicit claim =>  implicit request =>  lang =>
    RequestTimeout(views.html.common.session_timeout(startPage))
  }

  def error = endingOnError {implicit claim =>  implicit request =>  lang =>
    InternalServerError(views.html.common.error(startPage)(request,lang))
  }

  def errorCookie = endingOnError {implicit claim =>  implicit request =>  lang =>
    Unauthorized(views.html.common.error_cookie_retry(startPage)(request,lang))
  }

  def errorBrowserBackbutton = endingOnError {implicit claim =>  implicit request =>  lang =>
    BadRequest(views.html.common.errorBrowserBackbutton(startPage))
  }

  def thankyou = ending {implicit claim =>  implicit request =>  lang =>

    if (getProperty("mailer.enabled",default=false)){
      val preview = claim.questionGroup[PreviewModel].getOrElse(PreviewModel())
      if (preview.email.isDefined) EmailServices.sendEmail to preview.email.get
    }

    Ok(views.html.common.thankYouClaim(lang))
  }

}
