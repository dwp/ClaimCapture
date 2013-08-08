package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import controllers.Mappings
import models.view.CachedClaim
import models.domain.{MoreAboutThePerson, PreviousCarerContactDetails}
import utils.helpers.CarersForm._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

object G5PreviousCarerContactDetails extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "mobileNumber" -> optional(text verifying validPhoneNumber)
    )(PreviousCarerContactDetails.apply)(PreviousCarerContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val claimedAllowanceBefore: Boolean = claim.questionGroup(MoreAboutThePerson) match {
      case Some(m: MoreAboutThePerson) => m.claimedAllowanceBefore == Mappings.yes
      case _ => false
    }

    if (claimedAllowanceBefore)
      Ok(views.html.s4_care_you_provide.g5_previousCarerContactDetails(form.fill(PreviousCarerContactDetails), completedQuestionGroups(PreviousCarerContactDetails)))
    else
      claim.delete(PreviousCarerContactDetails) -> Redirect(routes.G6RepresentativesForThePerson.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g5_previousCarerContactDetails(formWithErrors, completedQuestionGroups(PreviousCarerContactDetails))),
      previousCarerContactDetails => claim.update(previousCarerContactDetails) -> Redirect(routes.G6RepresentativesForThePerson.present()))
  }
}