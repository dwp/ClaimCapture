package controllers.s2_about_you

import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.mappings.Mappings._
import models.domain.AbroadForMoreThan52Weeks
import models.view.Navigable
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.CarersForms._


object G5AbroadForMoreThan52Weeks extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "anyTrips" -> nonEmptyText.verifying(validYesNo),
    "tripDetails" -> optional(carersNonEmptyText(maxLength = 3000))
  )(AbroadForMoreThan52Weeks.apply)(AbroadForMoreThan52Weeks.unapply)
    .verifying(AbroadForMoreThan52Weeks.requiredTripDetails)
  )

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(AbroadForMoreThan52Weeks) { implicit claim => Ok(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(form.fill(AbroadForMoreThan52Weeks))(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>

    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "tripdetails.required", FormError("tripDetails", errorRequired))
        BadRequest(views.html.s2_about_you.g5_abroad_for_more_than_52_weeks(formWithErrorsUpdate)(lang))
      },
      abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks) -> Redirect(routes.G7OtherEEAStateOrSwitzerland.present())
    )

  } withPreview()
}