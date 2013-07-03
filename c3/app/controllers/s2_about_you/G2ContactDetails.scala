package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import play.api.data.validation.Constraints._

object G2ContactDetails extends Controller with Routing with CachedClaim {
  override val route = ContactDetails.id -> routes.G2ContactDetails.present

  val form = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying pattern("""[0-9 \-]{1,20}""".r, "constraint.invalid", "error.invalid")),
      "mobileNumber" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.AboutYou.id).takeWhile(_.id != ContactDetails.id)

  def present = claiming { implicit claim => implicit request =>
    val contactDetailsForm: Form[ContactDetails] = claim.questionGroup(ContactDetails.id) match {
      case Some(c: ContactDetails) => form.fill(c)
      case _ => form
    }

    Ok(views.html.s2_about_you.g2_contactDetails(contactDetailsForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g2_contactDetails(formWithErrors, completedQuestionGroups)),
      contactDetails => claim.update(contactDetails) -> Redirect(routes.G3TimeOutsideUK.present()))
  }
}