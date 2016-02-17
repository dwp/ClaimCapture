package controllers.s_about_you

import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.mappings.Mappings._
import play.api.data.{FormError, Form}
import models.domain.NationalityAndResidency
import utils.CommonValidation
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import play.api.i18n._

object GNationalityAndResidency extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val resideInUKMapping =
    "resideInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersNonEmptyText(maxLength = 35))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("error.text.required", YesNoWithText.validateOnNo _)

  val form = Form(mapping(
    "nationality" -> nonEmptyText.verifying(NationalityAndResidency.validNationality),
    "actualnationality" -> optional(carersNonEmptyText(maxLength = CommonValidation.NATIONALITY_MAX_LENGTH)),
    resideInUKMapping
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
    .verifying(NationalityAndResidency.actualNationalityRequired))

  def present = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    track(NationalityAndResidency) { implicit claim =>
      Ok(views.html.s_about_you.g_nationalityAndResidency(form.fill(NationalityAndResidency)))
    }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "actualnationality.required", FormError("actualnationality", errorRequired))
          .replaceError("resideInUK", "error.text.required", FormError("resideInUK.text", errorRequired))
        BadRequest(views.html.s_about_you.g_nationalityAndResidency(formWithErrorsUpdate))
      },
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.GAbroadForMoreThan52Weeks.present()))
  } withPreview()
}
