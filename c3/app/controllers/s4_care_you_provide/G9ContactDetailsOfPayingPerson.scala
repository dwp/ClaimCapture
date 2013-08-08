package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain.{MoreAboutTheCare, ContactDetailsOfPayingPerson}
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G9ContactDetailsOfPayingPerson extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text)
    )(ContactDetailsOfPayingPerson.apply)(ContactDetailsOfPayingPerson.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(MoreAboutTheCare) match {
      case Some(MoreAboutTheCare(_, _, "yes")) =>
        Ok(views.html.s4_care_you_provide.g9_contactDetailsOfPayingPerson(form.fill(ContactDetailsOfPayingPerson), completedQuestionGroups(ContactDetailsOfPayingPerson)))

      case _ =>
        Redirect(routes.G10BreaksInCare.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g9_contactDetailsOfPayingPerson(formWithErrors, completedQuestionGroups(ContactDetailsOfPayingPerson))),
      contactDetailsOfPayingPerson => claim.update(contactDetailsOfPayingPerson) -> Redirect(routes.G10BreaksInCare.present()))
  }
}