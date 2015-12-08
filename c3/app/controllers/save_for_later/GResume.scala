package controllers.save_for_later

import models.domain._
import models.view.{CachedClaim, Navigable}
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

  def present = newClaim { implicit claim => implicit request => implicit lang =>
    val savekeyuuid = createParamsMap(request.queryString).getOrElse("savekey", "")
    if (!getProperty("saveForLaterResumeEnabled", default = false)) {
      BadRequest(views.html.save_for_later.switchedOff(lang))
    }
    else if (savekeyuuid.equals("")) {
      BadRequest(views.html.save_for_later.resumeNotExist(lang))
    }
    else {
      // using an intermediate variable for status to allow easier testing to amend the status in debug
      val status = checkSaveForLaterInCache(savekeyuuid)
      status match {
        case "OK" => Ok(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = savekeyuuid))))
        case "FAILED-RETRY-LEFT2" => BadRequest(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = savekeyuuid)).withGlobalError(messagesApi("saveForLater.failed.new.triesleft2"))))
        case "FAILED-RETRY-LEFT1" => BadRequest(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = savekeyuuid)).withGlobalError(messagesApi("saveForLater.failed.new.triesleft1"))))
        case "FAILED-FINAL" => BadRequest(views.html.save_for_later.resumeFailedFinal(lang))
        case "EXPIRED" => BadRequest(views.html.save_for_later.resumeExpired(lang))
        case "NO-CLAIM" => BadRequest(views.html.save_for_later.resumeNotExist(lang))
        case s => BadRequest(views.html.save_for_later.resumeClaim(form.withGlobalError("Failure retrieving claim bad status:" + s)))
      }
    }
  }

  def submit = resumeClaim { implicit claim => implicit request => implicit lang =>
    if (!getProperty("saveForLaterResumeEnabled", default = false)) {
      BadRequest(views.html.save_for_later.switchedOff(lang))
    }
    else {
      form.bindEncrypted.fold(
        formWithErrors => {
          BadRequest(views.html.save_for_later.resumeClaim(formWithErrors))
        },
        resumeSaveForLater => {
          val retrievedSfl = resumeSaveForLaterFromCache(resumeSaveForLater, resumeSaveForLater.uuid)

          // If successful authenticate against the saved-for-later claim, then the saved claim will been restored into the current cache
          // So we retrieve it and redirect to last saved location. The ClaimHandling action will see the change in uuid and set the cookie uuid to match the resumed claim
          retrievedSfl match {
            case Some(sfl) if sfl.status.equals("OK") => {
              fromCache(resumeSaveForLater.uuid) match {
                case Some(resumedClaim) => resumedClaim -> Redirect(sfl.location)
                case _ => BadRequest(views.html.save_for_later.resumeClaim(form.withGlobalError("Unexpected failure retrieving claim from cache")))
              }
            }
            case Some(sfl) if sfl.status.equals("FAILED-RETRY-LEFT2") => BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft2"))))
            case Some(sfl) if sfl.status.equals("FAILED-RETRY-LEFT1") => BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft1"))))
            case Some(sfl) if sfl.status.equals("FAILED-FINAL") => Ok(views.html.save_for_later.resumeFailedFinal(lang))
            case Some(sfl) if sfl.status.equals("EXPIRED") => Ok(views.html.save_for_later.resumeExpired(lang))
            case _ => Ok(views.html.save_for_later.resumeNotExist(lang))
          }
        })
    }
  }
}

