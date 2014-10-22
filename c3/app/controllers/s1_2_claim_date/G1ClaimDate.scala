package controllers.s1_2_claim_date

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._

object G1ClaimDate extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "dateOfClaim" -> dayMonthYear.verifying(validDate)
  )(ClaimDate.apply)(ClaimDate.unapply))

  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(ClaimDate) { implicit claim => Ok(views.html.s1_2_claim_date.g1_claimDate(form.fill(ClaimDate))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s1_2_claim_date.g1_claimDate(formWithErrors)),
      claimDate => claim.update(claimDate) -> Redirect("/about-you/your-details"))
  }
}