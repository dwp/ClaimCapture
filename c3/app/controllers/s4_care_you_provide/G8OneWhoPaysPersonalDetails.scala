package controllers.s4_care_you_provide

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Mappings
import utils.helpers.CarersForm._

object G8OneWhoPaysPersonalDetails extends Controller with CareYouProvideRouting with Mappings.Name with CachedClaim {
  val form = Form(
    mapping(
      "organisation" -> optional(text(maxLength = hundred)),
      "title" -> optional(text(maxLength = 4)),
      "firstName" -> optional(text(maxLength = maxLength)),
      "middleName" -> optional(text(maxLength = maxLength)),
      "surname" -> optional(text(maxLength = maxLength)),
      "amount" -> optional(text verifying validDecimalNumber),
      "startDatePayment" -> optional(dayMonthYear.verifying(validDateOnly))
    )(OneWhoPaysPersonalDetails.apply)(OneWhoPaysPersonalDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val hasSomeonePaidYou: Boolean = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => m.hasSomeonePaidYou == Mappings.yes
      case _ => false
    }

    if (hasSomeonePaidYou)
      Ok(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(form.fill(OneWhoPaysPersonalDetails), completedQuestionGroups(OneWhoPaysPersonalDetails)))
    else
      claim.delete(OneWhoPaysPersonalDetails) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(formWithErrors, completedQuestionGroups(OneWhoPaysPersonalDetails))),
      oneWhoPaysPersonalDetails => claim.update(oneWhoPaysPersonalDetails) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present()))
  }
}