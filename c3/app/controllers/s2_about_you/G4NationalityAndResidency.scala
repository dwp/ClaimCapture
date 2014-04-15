package controllers.s2_about_you

import models.view.{CachedClaim, Navigable}
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.Mappings._
import play.api.data.{FormError, Form}
import models.domain.NationalityAndResidency
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText

object G4NationalityAndResidency extends Controller with CachedClaim with Navigable {
  val resideInUKMapping =
    "resideInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersNonEmptyText(maxLength = 35))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("error.text.required", YesNoWithText.validateOnNo _)

  val form = Form(mapping(
    "nationality" -> carersNonEmptyText(maxLength = 35),
    resideInUKMapping
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
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
          .replaceError("resideInUK", "error.text.required", FormError("resideInUK.text", "error.required"))
        BadRequest(views.html.s2_about_you.g4_nationalityAndResidency(formWithErrorsUpdate))
      },
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.G5AbroadForMoreThan52Weeks.present()))
  }
}
