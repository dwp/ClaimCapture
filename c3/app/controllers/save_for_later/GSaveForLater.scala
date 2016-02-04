package controllers.save_for_later

import models.domain.Claim
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.i18n._
import play.api.mvc.Controller
import services.EmailServices
import utils.helpers.CarersCrypto
import app.ConfigProperties._
import scala.language.reflectiveCalls
import play.api.mvc._

object GSaveForLater extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  def present(resumePath: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    getProperty("saveForLaterSaveEnabled", default = false) match {
      case false => BadRequest(views.html.common.switchedOff("sfl-save", request2lang))
      case true => Ok(views.html.save_for_later.saveClaimSuccess(resumePath, request2lang))
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    getProperty("saveForLaterSaveEnabled", default = false) match {
      case false => BadRequest(views.html.common.switchedOff("sfl-save", request2lang))
      case true => processSaveForLater(request.body.asFormUrlEncoded.get, claim, request2lang, request)
    }
  }

  def processSaveForLater(parameters: Map[String, Seq[String]], claim: Claim, lang: Lang, request: Request[AnyContent]) = {
    val updatedClaim = claim.update(createSaveForLaterMap(parameters))
    val resumePath=claim.navigation.saveForLaterRoute(iterationResumePath(parameters)).toString
    saveForLaterInCache(updatedClaim, resumePath)
    EmailServices.sendSaveForLaterEmail(claim, request)
    updatedClaim -> Redirect(controllers.save_for_later.routes.GSaveForLater.present(resumePath))
  }

  def iterationResumePath(parameters: Map[String, Seq[String]]):String={
    (parameters.contains("iterationID"), parameters.contains("hasBreakEnded.date.year"), parameters.contains("employerName")) match{
      case( true, true, false ) => "/breaks/break/"+ parameters.get("iterationID").head(0).toString()
      case( true, false, true ) => "/employment/job-details/"+ parameters.get("iterationID").head(0).toString()
      case(_,_,_) => ""
    }
  }

  def createSaveForLaterMap(parameters: Map[String, Seq[String]]) = {
    parameters.map { case (k, v) => {
      k match {
        case "csrfToken" | "action" | "jsEnabled" => k
        case _ => CarersCrypto.decryptAES(k)
      }
    } -> v.mkString
    }
  }
}
