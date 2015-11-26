package controllers

import app.ConfigProperties._
import models.domain.{CircumstancesDeclaration, ContactDetails}
import play.api.Play._
import play.api.mvc.Controller
import models.view.CachedChangeOfCircs
import services.EmailServices
import play.api.i18n._

object CircsEnding extends Controller with CachedChangeOfCircs with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def timeout = endingOnError {implicit claim => implicit request => implicit lang =>
    RequestTimeout(views.html.common.session_timeout(startPage))
  }

  def error = endingOnError {implicit claim => implicit request => implicit lang =>
    InternalServerError(views.html.common.error(startPage))
  }

  def errorCookie = endingOnError {implicit claim => implicit request => implicit lang =>
    Unauthorized(views.html.common.error_cookie_retry(startPage))
  }

  def errorBrowserBackbutton = endingOnError {implicit claim => implicit request => implicit lang =>
    BadRequest(views.html.common.errorBrowserBackbutton(startPage))
  }

  def thankyou = ending {implicit claim => implicit request => implicit lang =>

    Ok(views.html.common.thankYouCircs(lang))
  }

}
