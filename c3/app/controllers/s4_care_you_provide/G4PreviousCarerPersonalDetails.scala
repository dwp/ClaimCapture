package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import controllers.Mappings
import models.view.CachedClaim
import models.domain.{MoreAboutThePerson, PreviousCarerPersonalDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

object G4PreviousCarerPersonalDetails extends Controller with CareYouProvideRouting with Mappings.Name with CachedClaim {
  val form = Form(
    mapping(
      "firstName" -> optional(text(maxLength = maxLength)),
      "middleName" -> optional(text(maxLength = maxLength)),
      "surname" -> optional(text(maxLength = maxLength)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> optional(dayMonthYear.verifying(validDateOnly))
    )(PreviousCarerPersonalDetails.apply)(PreviousCarerPersonalDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson) match {
      case Some(m: MoreAboutThePerson) => m.claimedAllowanceBefore == Mappings.yes
      case _ => false
    }

    if (claimedAllowanceBefore) {
      Ok(views.html.s4_care_you_provide.g4_previousCarerPersonalDetails(form.fill(PreviousCarerPersonalDetails), completedQuestionGroups(PreviousCarerPersonalDetails)))
    } else {
      claim.delete(PreviousCarerPersonalDetails) -> Redirect(routes.G5PreviousCarerContactDetails.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g4_previousCarerPersonalDetails(formWithErrors, completedQuestionGroups(PreviousCarerPersonalDetails))),
      currentForm => claim.update(currentForm) -> Redirect(routes.G5PreviousCarerContactDetails.present()))
  }
}