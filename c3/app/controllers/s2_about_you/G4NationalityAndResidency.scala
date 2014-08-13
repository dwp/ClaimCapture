package controllers.s2_about_you

import models.view.{CachedClaim, Navigable}
import play.api.Logger
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.Mappings._
import play.api.data.{FormError, Form}
import models.domain.NationalityAndResidency
import utils.helpers.CarersForm._
import models.yesNo.YesNo

object G4NationalityAndResidency extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "nationality" -> nonEmptyText.verifying(NationalityAndResidency.validNationality),
    "residency" -> optional(carersNonEmptyText(maxLength = 35))
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
    .verifying(NationalityAndResidency.residencyRequired)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    track(NationalityAndResidency) { implicit claim =>
      Ok(views.html.s2_about_you.g4_nationalityAndResidency(form.fill(NationalityAndResidency)))
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "residency.required", FormError("residency", "error.required"))
        BadRequest(views.html.s2_about_you.g4_nationalityAndResidency(formWithErrorsUpdate))
      },
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.G5AbroadForMoreThan52Weeks.present()))
  }
}
