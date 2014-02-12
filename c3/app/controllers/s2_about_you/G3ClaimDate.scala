package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._

object G3ClaimDate extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "dateOfClaim" -> dayMonthYear.verifying(validDate)
  )(ClaimDate.apply)(ClaimDate.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(ClaimDate) { implicit claim => Ok(views.html.s2_about_you.g3_claimDate(form.fill(ClaimDate))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g3_claimDate(formWithErrors)),
      claimDate => claim.update(claimDate) -> Redirect(routes.G4NationalityAndResidency.present()))
  }
}