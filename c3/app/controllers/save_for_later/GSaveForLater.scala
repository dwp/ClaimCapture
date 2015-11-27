package controllers.save_for_later

import models.domain._
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.{Form}
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc.{Controller}
import utils.helpers.CarersForm._

import scala.language.reflectiveCalls

object GSaveForLater extends Controller with CachedClaim with Navigable with I18nSupport {

  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val resideInUKMapping =
    "resideInUK" -> mapping(
      "answer" -> optional(text),
      "text" -> optional(text)
    )(ResideInUK.apply)(ResideInUK.unapply)

  val form = Form(mapping(
    "nationality" -> optional(text),
    "actualnationality" -> optional(text),
    resideInUKMapping,
    "anyTrips"-> optional(text),
    "tripDetails"-> optional(text)
  )(SaveForLater.apply)(SaveForLater.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    Ok(views.html.save_for_later.saveClaimSuccess(lang))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
         BadRequest(views.html.save_for_later.saveClaimSuccess(lang))
      },
      saveform =>{
        val updatedClaim=claim.copy(saveForLater=saveform)(claim.navigation)
        updatedClaim->Redirect(routes.GSaveForLater.present())
      }
    )
  }
}
