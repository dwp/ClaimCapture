package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Routing
import models.domain.NormalResidenceAndCurrentLocation
import controllers.Mappings._
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._

object G5otherEEAStateOrSwitzerland extends Controller with Routing with CachedClaim {
  override val route = "TODO" -> routes.G5otherEEAStateOrSwitzerland.present

  /*val form = Form(
    mapping(
      liveMapping,
      "inGBNow" -> nonEmptyText.verifying(validYesNo)
    )(NormalResidenceAndCurrentLocation.apply)(NormalResidenceAndCurrentLocation.unapply)
  )*/

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g5_otherEEAStateOrSwitzerland(/*form*/))
    /*val currentForm: Form[NormalResidenceAndCurrentLocation] = claim.questionGroup(NormalResidenceAndCurrentLocation) match {
      case Some(n: NormalResidenceAndCurrentLocation) => form.fill(n)
      case _ => form
    }

    Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(currentForm))*/
  }

  def submit = claiming { implicit claim => implicit request =>
    Ok("")
    /*form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("liveInUK", FormError("liveInUK.whereDoYouLive", "error.required"))
        BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrorsUpdate))
      },
      normalResidenceAndCurrentLocation => claim.update(normalResidenceAndCurrentLocation) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present()))*/
  }
}