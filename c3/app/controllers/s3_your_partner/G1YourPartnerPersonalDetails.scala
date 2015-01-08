package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.text
import play.api.data.Forms.optional
import play.api.mvc.{Request, AnyContent, Controller, Action}
import controllers.mappings.Mappings.errorRequired
import controllers.mappings.Mappings.Name
import controllers.mappings.NINOMappings._
import controllers.mappings.Mappings.validDate
import controllers.mappings.Mappings.validYesNo
import controllers.mappings.Mappings.dayMonthYear
import controllers.mappings.Mappings.validNationality
import models.domain.YourPartnerPersonalDetails
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm.formBinding
import YourPartner.presentConditionally
import controllers.CarersForms.carersNonEmptyText
import controllers.CarersForms.carersText
import models.domain.Claim
import play.api.i18n.Lang
import models.view.CachedClaim.ClaimResult
import controllers.mappings.Mappings

object G1YourPartnerPersonalDetails extends Controller with CachedClaim with Navigable {

  def form(implicit claim: Claim):Form[YourPartnerPersonalDetails] = Form(mapping(
    "title" -> optional(carersNonEmptyText(maxLength = Mappings.four)),
    "firstName" -> optional(carersNonEmptyText(maxLength = Mappings.seventeen)),
    "middleName" -> optional(carersText(maxLength = Mappings.seventeen)),
    "surname" -> optional(carersNonEmptyText(maxLength = Name.maxLength)),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
    "dateOfBirth" -> optional(dayMonthYear.verifying(validDate)),
    "nationality" -> optional(text.verifying(validNationality)),
    "separated.fromPartner" -> optional(nonEmptyText.verifying(validYesNo)),
    "isPartnerPersonYouCareFor" -> optional(nonEmptyText.verifying(validYesNo)),
    "hadPartnerSinceClaimDate" -> nonEmptyText.verifying(validYesNo)
  )(YourPartnerPersonalDetails.apply)(YourPartnerPersonalDetails.unapply)
    .verifying("title.required", YourPartnerPersonalDetails.validateTitle _)
    .verifying("firstName.required", YourPartnerPersonalDetails.validateFirstName _)
    .verifying("surname.required", YourPartnerPersonalDetails.validateSurName _)
    .verifying("dateOfBirth.required", YourPartnerPersonalDetails.validateDateOfBirth _)
    .verifying("separated.fromPartner.required", YourPartnerPersonalDetails.validateSeperatedFromPartner _)
    .verifying("isPartnerPersonYouCareFor.required", YourPartnerPersonalDetails.validatePartnerPersonYoucareFor _)
    .verifying("nationality.required", YourPartnerPersonalDetails.validateNationalityIfPresent(_, claim))
  )

  def present:Action[AnyContent] = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    presentConditionally(yourPartnerPersonalDetails(lang),lang)
  }

  private def yourPartnerPersonalDetails(lang:Lang)(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(YourPartnerPersonalDetails) { implicit claim => Ok(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(form.fill(YourPartnerPersonalDetails))(lang)) }
  }

  def submit:Action[AnyContent] = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "title.required", FormError("title", errorRequired))
          .replaceError("", "firstName.required", FormError("firstName", errorRequired))
          .replaceError("", "surname.required", FormError("surname", errorRequired))
          .replaceError("", "dateOfBirth.required", FormError("dateOfBirth", errorRequired))
          .replaceError("", "separated.fromPartner.required", FormError("separated.fromPartner", errorRequired))
          .replaceError("", "isPartnerPersonYouCareFor.required", FormError("isPartnerPersonYouCareFor", errorRequired))
          .replaceError("", "nationality.required", FormError("nationality", errorRequired))
        BadRequest(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(formWithErrorsUpdate)(lang))
      },
      f => claim.update(f) -> Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
    )
  }.withPreview()
}

