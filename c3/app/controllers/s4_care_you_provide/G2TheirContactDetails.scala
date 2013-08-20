package controllers.s4_care_you_provide

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.domain.{ContactDetails, TheirPersonalDetails, TheirContactDetails}

object G2TheirContactDetails extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g2_theirContactDetails(form.fill(TheirContactDetails), completedQuestionGroups(TheirContactDetails)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g2_theirContactDetails(formWithErrors, completedQuestionGroups(TheirContactDetails))),
      theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.G3RelationshipAndOtherClaims.present()))
  }
}