package controllers.save_for_later

import models.domain.Claim
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersCrypto
import app.ConfigProperties._
import scala.language.reflectiveCalls

object GSaveForLater extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Ok(views.html.save_for_later.saveClaimSuccess(lang))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    getProperty("saveForLaterSaveEnabled", default = false) match {
      case false => BadRequest(views.html.save_for_later.saveClaimSuccess(lang))
      case true => processSaveForLater(request.body.asFormUrlEncoded.get, claim, lang)
    }
  }

  def processSaveForLater(parameters: Map[String, Seq[String]], claim: Claim, lang: Lang) = {
      val updatedClaim = claim.update(createSaveForLaterMap(parameters))
      saveForLaterInCache(updatedClaim, claim.navigation.current.toString)
      updatedClaim -> Redirect(controllers.save_for_later.routes.GSaveForLater.present())
  }

  def createSaveForLaterMap(parameters: Map[String, Seq[String]]) = {
    parameters.map { case (k,v) => {
      k match {
        case "csrfToken" => k
        case "action" => k
        case _ => CarersCrypto.decryptAES(k)
      }
    } -> v.mkString }
  }
}
