package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.{Claim, MoreAboutThePerson}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.Claim
import scala.Some

object G3MoreAboutThePerson extends Controller with Routing with CachedClaim {

  override val route = MoreAboutThePerson.id -> routes.G3MoreAboutThePerson.present

  val form = Form(
    mapping(
      "relationship" -> nonEmptyText,
      "armedForcesPayment" -> optional(text),
      "claimedAllowanceBefore" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutThePerson)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[MoreAboutThePerson] = claim.questionGroup(MoreAboutThePerson) match {
      case Some(m: MoreAboutThePerson) => form.fill(m)
      case _ => form
    }

    Ok(views.html.s4_care_you_provide.g3_moreAboutThePerson(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g3_moreAboutThePerson(formWithErrors, completedQuestionGroups)),
      moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(routes.G4PreviousCarerPersonalDetails.present()))
  }
}