package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._
import controllers.CarersForms._
import play.api.Logger

object G2ContactDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "howWeContactYou" -> carersNonEmptyText(maxLength = 35),
    "contactYouByTextphone" -> optional(text(maxLength = 4))
  )(ContactDetails.apply)(ContactDetails.unapply))

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    track(ContactDetails) { implicit claim => Ok(views.html.s2_about_you.g2_contactDetails(form.fill(ContactDetails))) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g2_contactDetails(formWithErrors)),
      contactDetails =>{ Logger.info(s"ContactDetails value $contactDetails");claim.update(contactDetails) -> Redirect(routes.G4NationalityAndResidency.present())})
  }
}