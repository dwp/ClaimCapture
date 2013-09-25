package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._

object G4ClaimDate extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "dateOfClaim" -> dayMonthYear.verifying(validDate)
  )(ClaimDate.apply)(ClaimDate.unapply))

  def present = executeOnForm {implicit claim => implicit request =>
    track(ClaimDate) { implicit claim => Ok(views.html.s2_about_you.g4_claimDate(form.fill(ClaimDate))) }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g4_claimDate(formWithErrors)),
      claimDate => claim.update(claimDate) -> Redirect(routes.G5MoreAboutYou.present()))
  }
}