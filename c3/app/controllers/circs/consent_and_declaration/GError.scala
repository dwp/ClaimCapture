package controllers.circs.consent_and_declaration

import play.api.Play._
import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.i18n._

object GError extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(models.domain.Error) { implicit circs => Ok(views.html.circs.consent_and_declaration.error()) }
  }
}
