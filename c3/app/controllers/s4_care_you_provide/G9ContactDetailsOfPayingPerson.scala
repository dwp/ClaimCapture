package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G9ContactDetailsOfPayingPerson extends Controller with Routing with CachedClaim {

  override val route = ContactDetailsOfPayingPerson.id -> routes.G9ContactDetailsOfPayingPerson.present

  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text)
    )(ContactDetailsOfPayingPerson.apply)(ContactDetailsOfPayingPerson.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ContactDetailsOfPayingPerson)

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(MoreAboutTheCare) match {
      case Some(MoreAboutTheCare(_, _, _, "yes")) => {
        val contactDetailsOfPayingPersonForm: Form[ContactDetailsOfPayingPerson] = claim.questionGroup(ContactDetailsOfPayingPerson) match {
          case Some(c: ContactDetailsOfPayingPerson) => form.fill(c)
          case _ => form
        }

        Ok(views.html.s4_care_you_provide.g9_contactDetailsOfPayingPerson(contactDetailsOfPayingPersonForm, completedQuestionGroups))
      }

      case _ => Redirect(routes.G10BreaksInCare.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g9_contactDetailsOfPayingPerson(formWithErrors, completedQuestionGroups)),
      contactDetailsOfPayingPerson => claim.update(contactDetailsOfPayingPerson) -> Redirect(routes.G10BreaksInCare.present()))
  }
}