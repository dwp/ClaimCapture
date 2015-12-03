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
    // using an intermediate variable for status to allow easier testing to amend the status in debug
    val status = checkSaveForLaterInCache(savekeyuuid)
    status match {
      case "OK" => Ok(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = savekeyuuid))))
      case "FAILED-RETRY" => Ok(views.html.save_for_later.resumeClaim(form.fill(ResumeSaveForLater(uuid = savekeyuuid))))
      case "FAILED-FINAL" => Ok(views.html.save_for_later.resumeFailedFinal(lang))
      case "EXPIRED" => Ok(views.html.save_for_later.resumeExpired(lang))
      case "NO-CLAIM" => Ok(views.html.save_for_later.resumeNotExist(lang))
      case s => {
        BadRequest(views.html.save_for_later.resumeClaim(form.withError("surname", "Failure retrieving claim bad status:" + s)))
      }
    }
  }

  def submit = resumeClaim { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.save_for_later.resumeClaim(formWithErrors))
      },
      resumeSaveForLater => {
        val retrievedSfl = resumeSaveForLaterFromCache(resumeSaveForLater, resumeSaveForLater.uuid)

        // If successful authentication against the saved claim, then saved claim should have been restored into the current cache
        // resumeClaim will have set the cookie uid to match the resumed claim
        retrievedSfl match {
          case Some(sfl) if sfl.status.equals("OK") => claim -> Redirect(sfl.location)
          case Some(sfl) if sfl.status.equals("FAILED-RETRY") && sfl.remainingAuthenticationAttempts==2 => BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft2"))))
          case Some(sfl) if sfl.status.equals("FAILED-RETRY") && sfl.remainingAuthenticationAttempts==1 => BadRequest(views.html.save_for_later.resumeClaim(form.fill(resumeSaveForLater).withGlobalError(messagesApi("saveForLater.failed.triesleft1"))))
          case Some(sfl) if sfl.status.equals("FAILED-FINAL") => Ok(views.html.save_for_later.resumeFailedFinal(lang))
          case Some(sfl) if sfl.status.equals("EXPIRED") => Ok(views.html.save_for_later.resumeExpired(lang))
          case _ => Ok(views.html.save_for_later.resumeNotExist(lang))
        }
      })
  }
}

