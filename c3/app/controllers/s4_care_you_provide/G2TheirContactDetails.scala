package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.domain.{ContactDetails, TheirPersonalDetails, TheirContactDetails}
import models.domain.YourPartnerContactDetails

object G2TheirContactDetails extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails) match {
      case Some(t: TheirPersonalDetails) => t.liveAtSameAddress == yes
      case _ => false
    }

    val theirContactDetailsPrePopulatedForm = if (liveAtSameAddress) {
      claim.questionGroup(ContactDetails) match {
        case Some(cd: ContactDetails) => form.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
        case _ => form
      }
    } else {
      claim.questionGroup(TheirContactDetails) match {
        case Some(t: TheirContactDetails) => form.fill(t)
        case _ => claim.questionGroup(YourPartnerContactDetails) match {// Get YourPartner address
            case Some(cd: YourPartnerContactDetails) => form.fill(TheirContactDetails(address = cd.address.get, postcode =  cd.postcode))
            case _ => form
          }
      }
    }

    Ok(views.html.s4_care_you_provide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm, completedQuestionGroups(TheirContactDetails)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g2_theirContactDetails(formWithErrors, completedQuestionGroups(TheirContactDetails))),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.G3MoreAboutThePerson.present()))
  }
}