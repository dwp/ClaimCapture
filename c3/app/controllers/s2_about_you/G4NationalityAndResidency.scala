package controllers.s2_about_you

import models.view.{CachedClaim, Navigable}
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.Mappings._
import play.api.data.Form
import models.domain.NationalityAndResidency
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText

object G4NationalityAndResidency extends Controller with CachedClaim with Navigable {
  val resideInUKMapping =
    "resideInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(carersNonEmptyText(maxLength = 35))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val form = Form(mapping(
    "nationality" -> carersNonEmptyText(maxLength = 35),
    resideInUKMapping
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    track(NationalityAndResidency) { implicit claim =>
      Ok(views.html.s2_about_you.g4_nationalityAndResidency(form.fill(NationalityAndResidency)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g4_nationalityAndResidency(formWithErrors)),
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.G7OtherEEAStateOrSwitzerland.present()))
  }
}
