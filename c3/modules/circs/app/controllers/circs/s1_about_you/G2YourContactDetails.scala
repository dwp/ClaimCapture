package controllers.circs.s1_about_you

import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.CircumstancesYourContactDetails
import utils.helpers.CarersForm._


object G2YourContactDetails extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> optional(text verifying validPhoneNumber),
    "mobileNumber" -> optional(text verifying validPhoneNumber)
  )(CircumstancesYourContactDetails.apply)(CircumstancesYourContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(CircumstancesYourContactDetails) { implicit claim => Ok(views.html.circs.s1_about_you.g2_yourContactDetails(form.fill(CircumstancesYourContactDetails))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_about_you.g2_yourContactDetails(formWithErrors)),
      circumstancesYourContactDetails => claim.update(circumstancesYourContactDetails) -> Redirect(routes.G3DetailsOfThePersonYouCareFor.present()))
  }

}
