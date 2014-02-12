package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Request, AnyContent, Controller}
import controllers.Mappings._
import models.domain.YourPartnerPersonalDetails
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm.formBinding
import YourPartner._
import controllers.CarersForms._
import models.domain.Claim

object G1YourPartnerPersonalDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> nonEmptyText(maxLength = 4),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "nationality" -> optional(text.verifying(validNationality)),
    "separated.fromPartner" -> nonEmptyText.verifying(validYesNo),
    "isPartnerPersonYouCareFor" -> nonEmptyText.verifying(validYesNo)
  )(YourPartnerPersonalDetails.apply)(YourPartnerPersonalDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    presentConditionally(yourPartnerPersonalDetails)
  }

  def yourPartnerPersonalDetails(implicit claim: Claim, request: Request[AnyContent]): ClaimResult = {
    track(YourPartnerPersonalDetails) { implicit claim => Ok(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(form.fill(YourPartnerPersonalDetails))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(formWithErrors)),
      f => claim.update(f) -> Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
    )
  }
}

