package controllers.s2_about_you

import models.view.{CachedClaim, Navigable}
import play.api.mvc.Controller
import controllers.CarersForms._
import play.api.data.Forms._
import controllers.Mappings._
import play.api.data.Form
import models.domain.NationalityAndResidency
import utils.helpers.CarersForm._

object G3NationalityAndResidency extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "nationality" -> carersText(maxLength = 35),
    "resideInUK" -> nonEmptyText.verifying(validYesNo),
    "residence" -> optional(carersText(maxLength = 35))
  )(NationalityAndResidency.apply)(NationalityAndResidency.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    track(NationalityAndResidency) { implicit claim =>
      Ok(views.html.s2_about_you.g3_nationalityAndResidency(form.fill(NationalityAndResidency)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g3_nationalityAndResidency(formWithErrors)),
      nationalityAndResidency => claim.update(nationalityAndResidency) -> Redirect(routes.G4ClaimDate.present()))
  }
}
