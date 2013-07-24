package controllers.s5_time_spent_abroad

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.NormalResidenceAndCurrentLocation
import controllers.Mappings._
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._

object G1NormalResidenceAndCurrentLocation extends Controller with CachedClaim {
  val liveMapping =
    "liveInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whereDoYouLive" -> optional(nonEmptyText(maxLength = sixty))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val form = Form(
    mapping(
      call(routes.G1NormalResidenceAndCurrentLocation.present()),
      liveMapping,
      "inGBNow" -> nonEmptyText.verifying(validYesNo)
    )(NormalResidenceAndCurrentLocation.apply)(NormalResidenceAndCurrentLocation.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(form.fill(NormalResidenceAndCurrentLocation)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("liveInUK", FormError("liveInUK.whereDoYouLive", "error.required"))
        BadRequest(views.html.s5_time_spent_abroad.g1_normalResidenceAndCurrentLocation(formWithErrorsUpdate))
      },
      normalResidenceAndCurrentLocation => claim.update(normalResidenceAndCurrentLocation) -> Redirect(routes.G2AbroadForMoreThan4Weeks.present()))
  }
}