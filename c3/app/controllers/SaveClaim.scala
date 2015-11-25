package controllers

import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.i18n._
import play.api.mvc.Controller

import scala.language.reflectiveCalls

object SaveClaim extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Ok(views.html.saveClaim.saveClaimSuccess(lang))
  }
}
