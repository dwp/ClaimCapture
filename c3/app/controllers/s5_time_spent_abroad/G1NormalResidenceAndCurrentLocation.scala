package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import models.domain.NormalResidenceAndCurrentLocation

object G1NormalResidenceAndCurrentLocation extends Controller with Routing with CachedClaim {
  override val route = NormalResidenceAndCurrentLocation.id -> routes.G1NormalResidenceAndCurrentLocation.present

  implicit val form = Form(
    mapping(
      "normallyLiveInUK" -> nonEmptyText,
      "whereDoYouNormallyLive" -> optional(nonEmptyText(maxLength = 60)),
      "inGBNow" -> nonEmptyText
    )(NormalResidenceAndCurrentLocation.apply)(NormalResidenceAndCurrentLocation.unapply))

  val whereDoYouNormallyLiveForm = Form(
    single(
      "whereDoYouNormallyLive" -> nonEmptyText(maxLength = 60)
    ))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    def normallyLiveInUK(implicit n: NormalResidenceAndCurrentLocation): Form[NormalResidenceAndCurrentLocation] = {
      if (n.normallyLiveInUK == "no" && n.whereDoYouNormallyLive == None) form.fill(n).withError("whereDoYouNormallyLive", "error.required")
      else form
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        if (formWithErrors("normallyLiveInUK").value.getOrElse("yes") == "no") {
          whereDoYouNormallyLiveForm.bindFromRequest.fold(
            whereDoYouNormallyLiveForm => BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrors.withError("whereDoYouNormallyLive", "error.required"))),
            value => BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrors))
          )
        } else {
          BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrors))
        }
      },
      implicit normalResidenceAndCurrentLocation => {
        if (normallyLiveInUK.hasErrors) BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(normallyLiveInUK))
        else claim.update(normalResidenceAndCurrentLocation) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present())
      })
  }
}