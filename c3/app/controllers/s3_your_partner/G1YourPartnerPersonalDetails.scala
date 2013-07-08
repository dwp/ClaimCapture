package controllers.s3_your_partner

import controllers.Mappings.dayMonthYear
import controllers.Mappings.nino
import controllers.Mappings.sixty
import controllers.Mappings.validDate
import controllers.Mappings.validNinoOnly
import controllers.Mappings.validYesNo
import controllers.Routing
import models.domain.YourPartnerPersonalDetails
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding

object G1YourPartnerPersonalDetails extends Controller with Routing with CachedClaim {

  override val route = YourPartnerPersonalDetails.id -> routes.G1YourPartnerPersonalDetails.present

  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
      "otherNames" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "nationality" -> optional(text(maxLength = sixty)),
      "liveAtSameAddress" -> nonEmptyText.verifying(validYesNo)
    )(YourPartnerPersonalDetails.apply)(YourPartnerPersonalDetails.unapply))


  def present = claiming {
    implicit claim => implicit request =>

      if (claim.isSectionVisible(models.domain.YourPartner.id)) {
        val currentForm: Form[YourPartnerPersonalDetails] = claim.questionGroup(YourPartnerPersonalDetails) match {
          case Some(t: YourPartnerPersonalDetails) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(currentForm))
      }

      else Redirect(controllers.s4_care_you_provide.routes.G1TheirPersonalDetails.present())
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s3_your_partner.g1_yourPartnerPersonalDetails(formWithErrors)),
        f => claim.update(f) -> Redirect(routes.G2YourPartnerContactDetails.present()))
  }
}
