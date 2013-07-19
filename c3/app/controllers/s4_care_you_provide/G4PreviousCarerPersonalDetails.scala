package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Mappings
import models.view.CachedClaim
import models.domain.{Claim, MoreAboutThePerson, PreviousCarerPersonalDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

object G4PreviousCarerPersonalDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "call" -> ignored(routes.G4PreviousCarerPersonalDetails.present()),
      "firstName" -> optional(text(maxLength = sixty)),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> optional(dayMonthYear.verifying(validDateOnly))
    )(PreviousCarerPersonalDetails.apply)(PreviousCarerPersonalDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PreviousCarerPersonalDetails)

  def present = claiming { implicit claim => implicit request =>
    val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson) match {
      case Some(m: MoreAboutThePerson) => m.claimedAllowanceBefore == Mappings.yes
      case _ => false
    }

    if (claimedAllowanceBefore) {
      val currentForm = claim.questionGroup(PreviousCarerPersonalDetails) match {
        case Some(p: PreviousCarerPersonalDetails) => form.fill(p)
        case _ => form
      }

      Ok(views.html.s4_care_you_provide.g4_previousCarerPersonalDetails(currentForm, completedQuestionGroups))
    } else {
      claim.delete(PreviousCarerPersonalDetails) -> Redirect(routes.G5PreviousCarerContactDetails.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g4_previousCarerPersonalDetails(formWithErrors, completedQuestionGroups)),
      currentForm => claim.update(currentForm) -> Redirect(routes.G5PreviousCarerContactDetails.present()))
  }
}