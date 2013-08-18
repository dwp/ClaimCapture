package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._

object G1YourDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> nonEmptyText(maxLength = 4),
    "firstName" -> nonEmptyText(maxLength = Name.maxLength),
    "middleName" -> optional(text(maxLength = Name.maxLength)),
    "surname" -> nonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(text(maxLength = sixty)),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
    "nationality" -> nonEmptyText.verifying(validNationality),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "alwaysLivedUK" -> nonEmptyText.verifying(validYesNo),
    "maritalStatus" -> nonEmptyText(maxLength = 1)
  )(YourDetails.apply)(YourDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(YourDetails) { implicit claim => Ok(views.html.s2_about_you.g1_yourDetails(form.fill(YourDetails))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g1_yourDetails(formWithErrors)),
      yourDetails => claim.update(yourDetails) -> Redirect(routes.G2ContactDetails.present()))
  }
}