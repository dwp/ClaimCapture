package controllers.s_about_you

import play.api.Play._
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.mappings.Mappings._
import models.domain.AbroadForMoreThan52Weeks
import models.view.Navigable
import utils.helpers.CarersForm._
import models.view.CachedClaim
import controllers.CarersForms._
import play.api.i18n._


object GAbroadForMoreThan52Weeks extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "anyTrips" -> nonEmptyText.verifying(validYesNo),
    "tripDetails" -> optional(carersNonEmptyText(maxLength = 3000))
  )(AbroadForMoreThan52Weeks.apply)(AbroadForMoreThan52Weeks.unapply)
    .verifying(AbroadForMoreThan52Weeks.requiredTripDetails)
  )

  def present = claimingWithCheck {implicit claim => implicit request => implicit lang => 
    track(AbroadForMoreThan52Weeks) { implicit claim => Ok(views.html.s_about_you.g_abroad_for_more_than_52_weeks(form.fill(AbroadForMoreThan52Weeks))) }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit lang => 

    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "tripdetails.required", FormError("tripDetails", errorRequired))
        BadRequest(views.html.s_about_you.g_abroad_for_more_than_52_weeks(formWithErrorsUpdate))
      },
      abroadForMoreThan52Weeks => claim.update(abroadForMoreThan52Weeks) -> Redirect(routes.GOtherEEAStateOrSwitzerland.present())
    )

  } withPreview()
}
