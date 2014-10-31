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
    "nationality" -> nonEmptyText.verifying(NationalityAndResidency.validNationality),
    "actualnationality" -> optional(carersNonEmptyText(maxLength = 35)),
    resideInUKMapping,
    "maritalStatus" -> optional(carersNonEmptyText)
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
    .verifying(NationalityAndResidency.actualNationalityRequired)
    .verifying(NationalityAndResidency.maritalStatusRequired)
  )

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(NationalityAndResidency) { implicit claim =>
      Ok(views.html.s2_about_you.g4_nationalityAndResidency(form.fill(NationalityAndResidency))(lang))
    }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "actualnationality.required", FormError("actualnationality", "error.required"))
          .replaceError("", "maritalstatus.required", FormError("maritalStatus", "error.required"))
          .replaceError("resideInUK", "error.text.required", FormError("resideInUK.text", "error.required"))
        BadRequest(views.html.s2_about_you.g4_nationalityAndResidency(formWithErrorsUpdate)(lang))
      },
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.G5AbroadForMoreThan52Weeks.present()))
  }
}
