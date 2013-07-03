package controllers.s4_care_you_provide

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.{Mappings, Routing}
import utils.helpers.CarersForm._

object G8OneWhoPaysPersonalDetails extends Controller with Routing with CachedClaim {

  override val route = OneWhoPaysPersonalDetails.id -> routes.G8OneWhoPaysPersonalDetails.present

  val form = Form(
    mapping(
      "organisation" -> optional(text(maxLength = hundred)),
      "title" -> optional(text),
      "firstName" -> optional(text(maxLength = sixty)),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> optional(text(maxLength = sixty)),
      "amount" -> optional(text verifying validDecimalNumber),
      "startDatePayment" -> optional(dayMonthYear.verifying(validDate))
    )(OneWhoPaysPersonalDetails.apply)(OneWhoPaysPersonalDetails.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(_.id != OneWhoPaysPersonalDetails.id)

  def present = claiming { implicit claim => implicit request =>
    val hasSomeonePaidYou: Boolean = claim.questionGroup(MoreAboutTheCare.id) match {
      case Some(m: MoreAboutTheCare) => m.hasSomeonePaidYou == Mappings.yes
      case _ => false
    }

    if (hasSomeonePaidYou) {
      val currentForm = claim.questionGroup(OneWhoPaysPersonalDetails.id) match {
        case Some(o: OneWhoPaysPersonalDetails) => form.fill(o)
        case _ => form
      }

      Ok(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(currentForm, completedQuestionGroups))
    } else {
      claim.delete(OneWhoPaysPersonalDetails.id) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g8_oneWhoPaysPersonalDetails(formWithErrors, completedQuestionGroups)),
      oneWhoPaysPersonalDetails => claim.update(oneWhoPaysPersonalDetails) -> Redirect(routes.G9ContactDetailsOfPayingPerson.present()))
  }
}