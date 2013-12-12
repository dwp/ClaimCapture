package controllers.circs.s1_identification

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import controllers.Mappings._
import models.domain.DetailsOfThePersonYouCareFor
import utils.helpers.CarersForm._
import controllers.CarersForms._

object G3DetailsOfThePersonYouCareFor extends Controller with CachedChangeOfCircs with Navigable {
  val firstName = "firstName"
  val middleName = "middleName"
  val lastName = "lastName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"

  val form = Form(mapping(
    firstName -> carersNonEmptyText(maxLength = 17),
    middleName -> optional(carersText(maxLength = 17)),
    lastName -> carersNonEmptyText(maxLength = Name.maxLength),
    nationalInsuranceNumber -> nino.verifying(filledInNino, validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate)
  )(DetailsOfThePersonYouCareFor.apply)(DetailsOfThePersonYouCareFor.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(DetailsOfThePersonYouCareFor) {
      implicit circs => Ok(views.html.circs.s1_identification.g3_detailsOfThePersonYouCareFor(form.fill(DetailsOfThePersonYouCareFor)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_identification.g3_detailsOfThePersonYouCareFor(formWithErrors)),
      f => circs.update(f) -> Redirect(routes.Identification.completed())
    )
  }
}