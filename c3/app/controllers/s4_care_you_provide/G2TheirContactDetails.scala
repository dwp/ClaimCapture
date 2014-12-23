package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.{TheirPersonalDetails, TheirContactDetails}

object G2TheirContactDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "address" -> address.verifying(requiredAddress),
    "postcode" -> optional(text verifying validPostcode)
  )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    val liveAtSameAddress = claim.questionGroup[TheirPersonalDetails].exists(_.liveAtSameAddressCareYouProvide == yes)

    if (liveAtSameAddress) {
      Redirect(routes.G7MoreAboutTheCare.present())
    } else {
      val theirContactDetailsForm = claim.questionGroup[TheirContactDetails].map {
        form.fill
      }.getOrElse(form)

      track(TheirContactDetails) { implicit claim => Ok(views.html.s4_care_you_provide.g2_theirContactDetails(theirContactDetailsForm)(lang)) }
    }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g2_theirContactDetails(formWithErrors)(lang)),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.G7MoreAboutTheCare.present()))
  } withPreview()
}