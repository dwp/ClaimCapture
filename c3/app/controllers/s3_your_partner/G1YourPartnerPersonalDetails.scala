package controllers.s3_your_partner

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Request, AnyContent, Controller}
import controllers.Mappings._
import models.domain.{Claim, YourPartnerPersonalDetails}
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm.formBinding
import YourPartner._

object G1YourPartnerPersonalDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> nonEmptyText(maxLength = 4),
    "firstName" -> nonEmptyText(maxLength = Name.maxLength),
    "middleName" -> optional(text(maxLength = Name.maxLength)),
    "surname" -> nonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(text(maxLength = sixty)),
    "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "nationality" -> optional(text.verifying(validNationality)),
    "separated.fromPartner" -> nonEmptyText.verifying(validYesNo)
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
      f => claim.update(f) -> Redirect(routes.G4PersonYouCareFor.present()))
  }
}