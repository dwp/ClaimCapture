package controllers.s_about_you

import controllers.s_care_you_provide.{GTheirPersonalDetails}
import models.yesNo.YesNoMandWithAddress
import play.api.Logger
import play.api.Play._
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
import play.api.i18n._

object GContactDetails extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode),
    "howWeContactYou" -> optional(carersNonEmptyText.verifying(validPhoneNumberRequired)),
    "contactYouByTextphone" -> optional(text(maxLength = 4)),
    "wantsEmailContact" -> optional(carersNonEmptyText.verifying(validYesNo)),
    "mail" -> optional(email.verifying(Constraints.maxLength(254))),
    "mailConfirmation" -> optional(text(maxLength = 254))
  )(ContactDetails.apply)(ContactDetails.unapply)
    .verifying("error.email.match", emailConfirmation _)
    .verifying("error.email.required", emailRequired _)
    .verifying("error.wants.required", wantsEmailRequired _)
  )

  def present = claiming {implicit claim => implicit request => implicit lang => 
    track(ContactDetails) { implicit claim => Ok(views.html.s_about_you.g_contactDetails(form.fill(ContactDetails))) }
  }

  def submit = claiming {implicit claim => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedForm = formWithErrors.replaceError("","error.email.match",FormError("mailConfirmation","error.email.match"))
                                        .replaceError("","error.email.required",FormError("mail",errorRequired))
                                        .replaceError("","error.wants.required",FormError("wantsEmailContact",errorRequired))
        BadRequest(views.html.s_about_you.g_contactDetails(updatedForm))
      },
      (contactDetails: ContactDetails) =>{
        val theirPersonalDetailsQG: Option[TheirPersonalDetails] =  claim.questionGroup[TheirPersonalDetails]
        val liveAtSameAddress = theirPersonalDetailsQG.exists(_.theirAddress.answer == yes)

        //if previously, during the journey the carer selected that the caree lives at the same address,
        // and then he changes the address - update the caree address too
        val updatedClaim:Claim = if (liveAtSameAddress) {
          val addressForm: Form[YesNoMandWithAddress] =
            Form(GTheirPersonalDetails.addressMapping).fill(YesNoMandWithAddress(address = Some(contactDetails.address), postCode = contactDetails.postcode))
          val address:YesNoMandWithAddress = addressForm.fold(p => YesNoMandWithAddress(),p => p)
          claim.update(theirPersonalDetailsQG.get.copy(theirAddress = address))
        }else{
          claim
        }
        updatedClaim.update(contactDetails) -> Redirect(routes.GNationalityAndResidency.present())
      })
  } withPreview()
}
