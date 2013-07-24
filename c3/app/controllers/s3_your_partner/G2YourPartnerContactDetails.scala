package controllers.s3_your_partner

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{ContactDetails, YourPartnerPersonalDetails, YourPartnerContactDetails, Claim}
import utils.helpers.CarersForm._
import YourPartner._

object G2YourPartnerContactDetails extends Controller with CachedClaim {
  val formCall = routes.G2YourPartnerContactDetails.present()

  val form = Form(
    mapping(
      call(formCall),
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode)
    )(YourPartnerContactDetails.apply)(YourPartnerContactDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(YourPartnerContactDetails)

  def present = claiming { implicit claim => implicit request =>
    whenVisible(claim) {
      val liveAtSameAddress = claim.questionGroup(YourPartnerPersonalDetails) match {
        case Some(t: YourPartnerPersonalDetails) => t.liveAtSameAddress == yes
        case _ => false
      }

      val prePopulatedForm = if (liveAtSameAddress) {
        claim.questionGroup(ContactDetails) match {
          case Some(cd: ContactDetails) => form.fill(YourPartnerContactDetails(formCall, address = Some(cd.address), postcode = cd.postcode))
          case _ => form
        }
      } else {
        claim.questionGroup(YourPartnerContactDetails) match {
          case Some(t: YourPartnerContactDetails) => form.fill(t)
          case _ => form
        }
      }
      Ok(views.html.s3_your_partner.g2_yourPartnerContactDetails(prePopulatedForm, completedQuestionGroups))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g2_yourPartnerContactDetails(formWithErrors, completedQuestionGroups)),
      contactDetails => claim.update(contactDetails) -> Redirect(controllers.s3_your_partner.routes.G3MoreAboutYourPartner.present()))
  }
}