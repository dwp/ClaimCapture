package controllers.save_for_later

import models.domain._
import models.view.{ClaimHandling, CachedClaim, Navigable}
import play.api.Logger
import play.api.Play._
import play.api.data.{Form}
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc._
import scala.language.reflectiveCalls
import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._
import utils.helpers.CarersForm._
import app.ConfigProperties._

object GResume extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino, validNino),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "uuid" -> text
  )(ResumeSaveForLater.apply)(ResumeSaveForLater.unapply))

  def present = newClaim { implicit claim => implicit request => implicit request2lang =>
    val encuuid = createParamsMap(request.queryString).getOrElse("x", "")
    val uuid=claim.getDecryptedUuid(encuuid)
    if (!getProperty("saveForLaterResumeEnabled", default = false)) {
      BadRequest(views.html.save_for_later.switchedOff("sfl-resume", request2lang))
    }
    else if (encuuid.equals("")) {
      BadRequest(views.html.save_for_later.resumeNotExist(request2lang))
    }
    else {
      // using an intermediate variable for status to allow easier testing to amend the status in debug
      val status = checkSaveForLaterInCache(uuid)
      Logger.info(s"SFL resume present resuming claim $uuid with status $status")
      status match {
        case "OK" => Ok(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = encuuid))))
        case "FAILED-RETRY-LEFT2" => BadRequest(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = encuuid)).withGlobalError(messagesApi("saveForLater.failed.new.triesleft2"))))
        case "FAILED-RETRY-LEFT1" => BadRequest(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = encuuid)).withGlobalError(messagesApi("saveForLater.failed.new.triesleft1"))))
        case "FAILED-FINAL" => BadRequest(views.html.save_for_later.resumeFailedFinal(request2lang))
        case "EXPIRED" => BadRequest(views.html.save_for_later.resumeExpired(request2lang))
        case "NO-CLAIM" => BadRequest(views.html.save_for_later.resumeNotExist(request2lang))
        case s => BadRequest(views.html.save_for_later.resumeClaim(form.withGlobalError("Failure retrieving claim bad status:" + s)))
      }
    }
  }

  def submit = resumeClaim { implicit claim => implicit request => implicit request2lang =>
    if (!getProperty("saveForLaterResumeEnabled", default = false)) {
      BadRequest(views.html.save_for_later.switchedOff("sfl-resume", request2lang))
    }
    else {
      form.bindEncrypted.fold(
        formWithErrors => {
          BadRequest(views.html.save_for_later.resumeClaim(formWithErrors))
        },
        resumeSaveForLater => {
          val retrievedSfl = resumeSaveForLaterFromCache(resumeSaveForLater, claim.getDecryptedUuid(resumeSaveForLater.uuid))

          // If successful authenticate against the saved-for-later claim, then the saved claim will been restored into the current cache
          // So we retrieve it and redirect to last saved location. The ClaimHandling action will see the change in uuid and set the cookie uuid to match the resumed claim
          retrievedSfl match {
            case Some(sfl) if sfl.status.equals("OK") => {
              fromCache(request, claim.getDecryptedUuid(resumeSaveForLater.uuid)) match {
                case Some(resumedClaim) => resumedClaim -> {
                  Logger.info("SFL resume submit success resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
                  Redirect(sfl.location).withCookies(Cookie(ClaimHandling.C3VERSION, sfl.appVersion))
                }
                case _ => {
                  Logger.error("SFL resume submit failed resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
                  BadRequest(views.html.save_for_later.resumeClaim(form.withGlobalError("Unexpected failure retrieving claim from cache")))
                }
              }
            }
            case Some(sfl) if sfl.status.equals("FAILED-RETRY-LEFT2") => {
              Logger.error("SFL resume submit failed with status "+sfl.status+" when resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
              BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft2"))))
            }
            case Some(sfl) if sfl.status.equals("FAILED-RETRY-LEFT1") => {
              Logger.error("SFL resume submit failed with status "+sfl.status+" when resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
              BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft1"))))
            }
            case Some(sfl) if sfl.status.equals("FAILED-FINAL") => {
              Logger.error("SFL resume submit failed with status "+sfl.status+" when resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
              Ok(views.html.save_for_later.resumeFailedFinal(request2lang))
            }
            case Some(sfl) if sfl.status.equals("EXPIRED") => {
              Logger.error("SFL resume submit failed with status "+sfl.status+" when resuming claim "+resumeSaveForLater.uuid+" with appVersion "+sfl.appVersion)
              Ok(views.html.save_for_later.resumeExpired(request2lang))
            }
            case _ => {
              Logger.error("SFL resume submit failed claim when resuming claim "+resumeSaveForLater.uuid)
              Ok(views.html.save_for_later.resumeNotExist(request2lang))
            }
          }
        })
    }
  }
}

