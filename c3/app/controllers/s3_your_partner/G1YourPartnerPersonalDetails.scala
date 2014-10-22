package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.{Request, AnyContent, Controller}
import controllers.Mappings._
import models.domain.YourPartnerPersonalDetails
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm.formBinding
import YourPartner._
import controllers.CarersForms._
import models.domain.Claim
import play.api.i18n.Lang
import models.view.CachedClaim.ClaimResult

object G1YourPartnerPersonalDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> optional(carersNonEmptyText(maxLength = 4)),
    "firstName" -> optional(carersNonEmptyText(maxLength = 17)),
    "middleName" -> optional(carersText(maxLength = 17)),
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
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    presentConditionally(yourPartnerPersonalDetails)
  }

  def yourPartnerPersonalDetails(implicit claim: Claim, request: Request[AnyContent], lang:Lang): ClaimResult = {
    track(YourPartnerPersonalDetails) { implicit claim => Ok(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(form.fill(YourPartnerPersonalDetails))) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "title.required", FormError("title", "error.required"))
          .replaceError("", "firstName.required", FormError("firstName", "error.required"))
          .replaceError("", "surname.required", FormError("surname", "error.required"))
          .replaceError("", "dateOfBirth.required", FormError("dateOfBirth", "error.required"))
          .replaceError("", "separated.fromPartner.required", FormError("separated.fromPartner", "error.required"))
          .replaceError("", "isPartnerPersonYouCareFor.required", FormError("isPartnerPersonYouCareFor", "error.required"))
        BadRequest(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(formWithErrorsUpdate))
      },
      f => claim.update(f) -> Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
    )
  }
}

