package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import play.api.data.validation.Constraints._

object G2ContactDetails extends Controller with AboutYouRouting with CachedClaim with Navigable {
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> optional(text verifying pattern( """[0-9 \-]{1,20}""".r, "constraint.invalid", "error.invalid")),
    "contactYouByTextphone" -> optional(text(maxLength = 3).verifying(validYesNo)), // TODO [Scott] this is a new field that needs to be added to the XML.
    "mobileNumber" -> optional(text)
  )(ContactDetails.apply)(ContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(ContactDetails) { implicit claim => Ok(views.html.s2_about_you.g2_contactDetails(form.fill(ContactDetails))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g2_contactDetails(formWithErrors)),
      contactDetails => claim.update(contactDetails) -> Redirect(routes.G3TimeOutsideUK.present()))
  }
}