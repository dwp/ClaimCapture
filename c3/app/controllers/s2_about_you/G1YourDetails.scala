package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.CarersForms._

object G1YourDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> nonEmptyText(maxLength = 4),
    "firstName" -> carersNonEmptyText(maxLength = Name.maxLength),
    "middleName" -> optional(carersText(maxLength = Name.maxLength)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino,validNino),
    "nationality" -> nonEmptyText.verifying(validNationality),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "alwaysLivedUK" -> nonEmptyText.verifying(validYesNo),
    "maritalStatus" -> nonEmptyText(maxLength = 45)
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