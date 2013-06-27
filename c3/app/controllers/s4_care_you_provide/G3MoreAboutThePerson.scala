package controllers.s4_care_you_provide

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.MoreAboutThePerson
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import utils.helpers.CarersForm._

object G3MoreAboutThePerson extends Controller with Routing with CachedClaim {

  override val route = MoreAboutThePerson.id -> controllers.s4_care_you_provide.routes.G3MoreAboutThePerson.present

  val form = Form(
    mapping(
      "relationship" -> nonEmptyText,
      "armedForcesPayment" -> optional(text),
      "claimedAllowanceBefore" -> nonEmptyText
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  def present = claiming {
    implicit claim => implicit request =>
      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != MoreAboutThePerson.id)

      val currentForm: Form[MoreAboutThePerson] = claim.questionGroup(MoreAboutThePerson.id) match {
        case Some(t: MoreAboutThePerson) => form.fill(t)
        case _ => form
      }
      Ok(views.html.s4_careYouProvide.g3_moreAboutThePerson(currentForm, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g3_moreAboutThePerson(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
        moreAboutThePerson => claim.update(moreAboutThePerson) -> Redirect(controllers.s4_care_you_provide.routes.G4PreviousCarerPersonalDetails.present))
  }
}
