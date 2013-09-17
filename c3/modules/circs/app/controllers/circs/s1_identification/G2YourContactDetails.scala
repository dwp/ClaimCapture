package controllers.circs.s1_identification

import play.api.mvc.Controller
import models.view.{Navigable, CachedCircs}
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.CircumstancesYourContactDetails
import utils.helpers.CarersForm._


object G2YourContactDetails extends Controller with CachedCircs with Navigable {

  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> optional(text verifying validPhoneNumber),
    "mobileNumber" -> optional(text verifying validPhoneNumber)
  )(CircumstancesYourContactDetails.apply)(CircumstancesYourContactDetails.unapply))

  def present = executeOnForm {
    implicit claim => implicit request =>
      track(CircumstancesYourContactDetails) {
        implicit claim => {
          Ok(views.html.circs.s1_identification.g2_yourContactDetails(form.fill(CircumstancesYourContactDetails)))
        }
      }
  }

  def submit = executeOnForm {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.circs.s1_identification.g2_yourContactDetails(formWithErrors)),
        f => claim.update(f) -> {
          claim.isBot(f) match {
            case true => NotFound(views.html.errors.onHandlerNotFound(request)) // Send bot to 404 page.
            case false => Redirect(routes.G3DetailsOfThePersonYouCareFor.present())
          }
        }
      )
  }
}
