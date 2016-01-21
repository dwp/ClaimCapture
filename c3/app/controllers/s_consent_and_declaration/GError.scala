package controllers.s_consent_and_declaration

import play.api.Play._
import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable
import play.api.i18n._

object GError extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(models.domain.Error) { implicit claim => Ok(views.html.s_consent_and_declaration.g4_error(request2lang)) }
  }
}
