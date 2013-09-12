package controllers.circs.s1_identification

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedCircs}
import controllers.Mappings._
import models.domain.DetailsOfThePersonYouCareFor
import utils.helpers.CarersForm._

object G3DetailsOfThePersonYouCareFor extends Controller with CachedCircs with Navigable {
  val firstName = "firstName"
  val middleName = "middleName"
  val lastName = "lastName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"

  val form = Form(mapping(
    firstName -> nonEmptyText(maxLength = Name.maxLength),
    middleName -> optional(text(maxLength = Name.maxLength)),
    lastName -> nonEmptyText(maxLength = Name.maxLength),
    nationalInsuranceNumber -> nino.verifying(filledInNino, validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate)
  )(DetailsOfThePersonYouCareFor.apply)(DetailsOfThePersonYouCareFor.unapply))

  def present = executeOnForm { implicit claim => implicit request =>
    track(DetailsOfThePersonYouCareFor) { implicit claim => Ok(views.html.circs.s1_identification.g3_detailsOfThePersonYouCareFor(form.fill(DetailsOfThePersonYouCareFor))) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_identification.g3_detailsOfThePersonYouCareFor(formWithErrors)),
      f => claim.update(f) -> Redirect(routes.Identification.completed()))
  }

}