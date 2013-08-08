package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings

object G1YourDetails extends Controller with Mappings.Name with CachedClaim {
  val form = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 4),
      "firstName" -> nonEmptyText(maxLength = maxLength),
      "middleName" -> optional(text(maxLength = maxLength)),
      "surname" -> nonEmptyText(maxLength = maxLength),
      "otherNames" -> optional(text(maxLength = maxLength)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "nationality" -> nonEmptyText(maxLength = sixty),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "maritalStatus" -> nonEmptyText(maxLength = 1),
      "alwaysLivedUK" -> nonEmptyText.verifying(validYesNo)
    )(YourDetails.apply)(YourDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s2_about_you.g1_yourDetails(form.fill(YourDetails)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g1_yourDetails(formWithErrors)),
      yourDetails => claim.update(yourDetails) -> Redirect(routes.G2ContactDetails.present()))
  }
}