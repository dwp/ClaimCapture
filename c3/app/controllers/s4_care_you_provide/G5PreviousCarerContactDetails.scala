package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.{Mappings, Routing}
import models.view.CachedClaim
import models.domain.{Claim, MoreAboutThePerson, PreviousCarerContactDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

object G5PreviousCarerContactDetails extends Controller with Routing with CachedClaim {

  override val route = PreviousCarerContactDetails.id -> routes.G5PreviousCarerContactDetails.present

  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "mobileNumber" -> optional(text verifying validPhoneNumber)
    )(PreviousCarerContactDetails.apply)(PreviousCarerContactDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PreviousCarerContactDetails)

  def present = claiming { implicit claim => implicit request =>
    val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson.id) match {
      case Some(m: MoreAboutThePerson) => m.claimedAllowanceBefore == Mappings.yes
      case _ => false
    }

    if (claimedAllowanceBefore) {
      val currentForm: Form[PreviousCarerContactDetails] = claim.questionGroup(PreviousCarerContactDetails.id) match {
        case Some(p: PreviousCarerContactDetails) => form.fill(p)
        case _ => form
      }

      Ok(views.html.s4_care_you_provide.g5_previousCarerContactDetails(currentForm, completedQuestionGroups))
    } else claim.delete(PreviousCarerContactDetails.id) -> Redirect(routes.G6RepresentativesForThePerson.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g5_previousCarerContactDetails(formWithErrors, completedQuestionGroups)),
      previousCarerContactDetails => claim.update(previousCarerContactDetails) -> Redirect(routes.G6RepresentativesForThePerson.present()))
  }
}