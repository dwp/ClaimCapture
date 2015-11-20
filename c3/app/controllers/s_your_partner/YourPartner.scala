package controllers.s_your_partner

import play.api.Play._
import play.api.mvc.{Request, AnyContent, Controller}
import models.view.CachedClaim
import models.view.Navigable
import models.domain.Claim
import models.view.ClaimHandling.ClaimResult
import play.api.i18n._

object YourPartner extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def presentConditionally(c: => ClaimResult) (implicit claim: Claim, lang:Lang, request: Request[AnyContent]): ClaimResult = {
    if (models.domain.YourPartner.visible){
      c
    } else {
      redirect()
    }
  }

  private def redirect()(implicit claim: Claim, lang: Lang, messages: Messages, request: Request[AnyContent]): ClaimResult =
    claim -> Redirect(controllers.s_care_you_provide.routes.GTheirPersonalDetails.present())
}
