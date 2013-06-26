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
  override val route = ContactDetailsOfPayingPerson.id -> controllers.s4_care_you_provide.routes.G9ContactDetailsOfPayingPerson.present

  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text)
    )(ContactDetailsOfPayingPerson.apply)(ContactDetailsOfPayingPerson.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(MoreAboutTheCare.id) match {
      case Some(MoreAboutTheCare(_, _, _, "yes")) => {
        val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(_.id != ContactDetailsOfPayingPerson.id)

        val contactDetailsOfPayingPersonForm: Form[ContactDetailsOfPayingPerson] = claim.questionGroup(ContactDetailsOfPayingPerson.id) match {
          case Some(c: ContactDetailsOfPayingPerson) => form.fill(c)
          case _ => form
        }

        Ok(views.html.s4_careYouProvide.g9_contactDetailsOfPayingPerson(contactDetailsOfPayingPersonForm, completedQuestionGroups))
      }

      case _ => Redirect(routes.G10HasBreaks.present)
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(_.id != ContactDetailsOfPayingPerson.id)

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g9_contactDetailsOfPayingPerson(formWithErrors, completedQuestionGroups)),
      contactDetailsOfPayingPerson => claim.update(contactDetailsOfPayingPerson) -> Redirect(routes.G10HasBreaks.present))
  }
}