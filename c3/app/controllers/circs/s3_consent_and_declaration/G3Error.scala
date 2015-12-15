package controllers.circs.s3_consent_and_declaration

import play.api.Play._
import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.i18n._

object G3Error extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(models.domain.Error) { implicit circs => Ok(views.html.circs.s3_consent_and_declaration.g3_error()) }
  }
}
