package controllers.s4_care_you_provide

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Mappings
import utils.helpers.CarersForm._

object G8OneWhoPaysPersonalDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "call" -> ignored(routes.G8OneWhoPaysPersonalDetails.present()),
      "organisation" -> optional(text(maxLength = hundred)),
      "title" -> optional(text(maxLength = 4)),
      "firstName" -> optional(text(maxLength = sixty)),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> optional(text(maxLength = sixty)),
      "amount" -> optional(text verifying validDecimalNumber),
      "startDatePayment" -> optional(dayMonthYear.verifying(validDateOnly))
    )(OneWhoPaysPersonalDetails.apply)(OneWhoPaysPersonalDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OneWhoPaysPersonalDetails)

  def present = claiming { implicit claim => implicit request =>
    val hasSomeonePaidYou: Boolean = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => m.hasSomeonePaidYou == Mappings.yes
      case _ => false
    }

    if (hasSomeonePaidYou) {
      val currentForm = claim.questionGroup(OneWhoPaysPersonalDetails) match {
        case Some(o: OneWhoPaysPersonalDetails) => form.fill(o)
        case _ => form
      }

      Ok(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(currentForm, completedQuestionGroups))
    } else {
      claim.delete(OneWhoPaysPersonalDetails) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(formWithErrors, completedQuestionGroups)),
      oneWhoPaysPersonalDetails => claim.update(oneWhoPaysPersonalDetails) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present()))
  }
}