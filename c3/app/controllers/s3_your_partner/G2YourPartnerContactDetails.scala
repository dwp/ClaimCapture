package controllers.s3_your_partner

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.YourPartnerContactDetails
import utils.helpers.CarersForm._
import YourPartner.whenSectionVisible

object G2YourPartnerContactDetails extends Controller with YourPartnerRouting with CachedClaim {
  val form = Form( mapping(
    "address" -> optional(address),
    "postcode" -> optional(text verifying validPostcode)
  )(YourPartnerContactDetails.apply)(YourPartnerContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    whenSectionVisible {
      Ok(views.html.s3_your_partner.g2_yourPartnerContactDetails(form.fill(YourPartnerContactDetails), completedQuestionGroups(YourPartnerContactDetails)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g2_yourPartnerContactDetails(formWithErrors, completedQuestionGroups(YourPartnerContactDetails))),
      contactDetails => claim.update(contactDetails) -> Redirect(controllers.s3_your_partner.routes.G3MoreAboutYourPartner.present()))
  }
}