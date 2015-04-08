package controllers.s2_about_you

import controllers.s4_care_you_provide.G2TheirContactDetails
import play.api.data.validation.Constraints

import language.reflectiveCalls
import language.implicitConversions
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._
import controllers.CarersForms._
import EMail._

object G3ContactDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "howWeContactYou" -> carersNonEmptyText(maxLength = 35),
    "contactYouByTextphone" -> optional(text(maxLength = 4)),
    "wantsEmailContact" -> optional(carersNonEmptyText.verifying(validYesNo)),
    "mail" -> optional(email.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254))
  )(ContactDetails.apply)(ContactDetails.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)
  )

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    track(ContactDetails) { implicit claim => Ok(views.html.s2_about_you.g3_contactDetails(form.fill(ContactDetails))(lang)) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedForm = formWithErrors.replaceError("","error.email.match",FormError("mailConfirmation","error.email.match"))
                                        .replaceError("","error.email.required",FormError("mail",errorRequired))
                                        .replaceError("","error.wants.required",FormError("wantsEmailContact",errorRequired))
        BadRequest(views.html.s2_about_you.g3_contactDetails(updatedForm)(lang))
      },
      contactDetails =>{
        val liveAtSameAddress = claim.questionGroup[TheirPersonalDetails].exists(_.liveAtSameAddressCareYouProvide == yes)

        val updatedClaim = if (liveAtSameAddress) {
          val theirContactDetailsForm =
            G2TheirContactDetails.form.fill(TheirContactDetails(address = contactDetails.address, postcode = contactDetails.postcode))
          claim.update(theirContactDetailsForm.fold(p => TheirContactDetails(),p => p))
        }else{
          claim
        }

        updatedClaim.update(contactDetails) -> Redirect(routes.G4NationalityAndResidency.present())
      })
  } withPreview()
}