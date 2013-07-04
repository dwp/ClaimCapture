package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import models.domain.NormalResidenceAndCurrentLocation

object G1NormalResidenceAndCurrentLocation extends Controller with Routing with CachedClaim {
  override val route = "TODO" -> routes.G1NormalResidenceAndCurrentLocation.present

  implicit val form = Form(
    mapping(
      "normallyLiveInUK" -> nonEmptyText,
      "whereDoYouNormallyLive" -> optional(nonEmptyText)
    )(NormalResidenceAndCurrentLocation.apply)(NormalResidenceAndCurrentLocation.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    def normallyLiveInUK(implicit normalResidenceAndCurrentLocation: NormalResidenceAndCurrentLocation): Form[NormalResidenceAndCurrentLocation] = {
      if (normalResidenceAndCurrentLocation.normallyLiveInUK == "no" && normalResidenceAndCurrentLocation.whereDoYouNormallyLive == None) form.fill(normalResidenceAndCurrentLocation).withError("whereDoYouNormallyLive", "error.required")
      else form
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrors)),
      implicit normalResidenceAndCurrentLocation => {
        if (normallyLiveInUK.hasErrors) BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(normallyLiveInUK))
        else claim.update(normalResidenceAndCurrentLocation) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }
}