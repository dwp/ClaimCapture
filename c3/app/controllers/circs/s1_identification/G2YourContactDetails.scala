package controllers.circs.s1_identification

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.CircumstancesYourContactDetails
import utils.helpers.CarersForm._

object G2YourContactDetails extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> nonEmptyText.verifying(validPhoneNumber),
    "mobileNumber" -> optional(text verifying validPhoneNumber)
  )(CircumstancesYourContactDetails.apply)(CircumstancesYourContactDetails.unapply))

  def present = claiming { implicit circs => implicit request =>
    track(CircumstancesYourContactDetails) {
      implicit circs => {
        Ok(views.html.circs.s1_identification.g2_yourContactDetails(form.fill(CircumstancesYourContactDetails)))
      }
    }
  }

  def submit = claiming { implicit circs => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_identification.g2_yourContactDetails(formWithErrors)),
      f => circs.update(f) -> Redirect(routes.G3DetailsOfThePersonYouCareFor.present())
    )
  }
}
