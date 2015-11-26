package controllers.s_pay_details

import play.api.Play._
import play.api.mvc.{AnyContent, Request, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.Claim
import models.view.ClaimHandling.ClaimResult
import play.api.i18n._

object PayDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val redirectPath = Redirect(controllers.s_information.routes.GAdditionalInfo.present())

  def presentConditionally(c: => ClaimResult)(implicit claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult = {
    if (models.domain.PayDetails.visible) c
    else redirect(claim,request,lang)
  }

  private def redirect( claim: Claim, request: Request[AnyContent], lang: Lang): ClaimResult =
    claim -> redirectPath
}
