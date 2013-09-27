package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.{TheirPersonalDetails, ContactDetails, TheirContactDetails}

object G2TheirContactDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "address" -> address,
    "postcode" -> optional(text verifying validPostcode),
    "phoneNumber" -> optional(text verifying validPhoneNumber)
  )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val liveAtSameAddress = claim.questionGroup[TheirPersonalDetails].exists(_.liveAtSameAddressCareYouProvide == yes)

    val theirContactDetailsForm = if (liveAtSameAddress) {
      claim.questionGroup[ContactDetails].map { cd =>
        form.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
      }.getOrElse(form)
    } else {
      claim.questionGroup[TheirContactDetails].map {
        form.fill
      }.getOrElse(form)
    }

    track(TheirContactDetails) { implicit claim => Ok(views.html.s4_care_you_provide.g2_theirContactDetails(theirContactDetailsForm)) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g2_theirContactDetails(formWithErrors)),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.G3RelationshipAndOtherClaims.present()))
  }
}