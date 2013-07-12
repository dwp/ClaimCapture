package controllers.s4_care_you_provide

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._

object G1TheirPersonalDetails extends Controller with Routing with CachedClaim {

  override val route = TheirPersonalDetails.id -> routes.G1TheirPersonalDetails.present

  val form = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 4),
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "liveAtSameAddress" -> nonEmptyText.verifying(validYesNo))(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner.id)

    val isPartnerPersonYouCareFor: Boolean = if (claim.isSectionVisible(models.domain.YourPartner.id)) {
      claim.questionGroup(PersonYouCareFor) match {
        case Some(t: PersonYouCareFor) => t.isPartnerPersonYouCareFor == "yes" // Get value the user selected previously.
        case _ => false
      }
    } else { false }

    val currentForm = if (isPartnerPersonYouCareFor) {
      claim.questionGroup(YourPartnerPersonalDetails) match {
        case Some(t: YourPartnerPersonalDetails) => form.fill(TheirPersonalDetails(title = t.title, firstName = t.firstName, middleName = t.middleName, surname = t.surname, nationalInsuranceNumber = t.nationalInsuranceNumber, dateOfBirth = t.dateOfBirth, liveAtSameAddress = t.liveAtSameAddress)) // Pre-populate form with values from YourPartnerPersonalDetails
        case _ => form // Blank form (user can only get here if they skip sections by manually typing URL).
      }
    } else {
      claim.questionGroup(TheirPersonalDetails) match {
        case Some(t: TheirPersonalDetails) => form.fill(t) // Fill from cache.
        case _ => form // Blank form.
      }
    }

    Ok(views.html.s4_care_you_provide.g1_theirPersonalDetails(currentForm, showYourPartnerSection))
  }

  def submit = claiming { implicit claim => implicit request =>
    val showYourPartnerSection = claim.isSectionVisible(YourPartner.id)

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g1_theirPersonalDetails(formWithErrors, showYourPartnerSection)),
      theirPersonalDetails => claim.update(theirPersonalDetails) -> Redirect(routes.G2TheirContactDetails.present()))
  }
}