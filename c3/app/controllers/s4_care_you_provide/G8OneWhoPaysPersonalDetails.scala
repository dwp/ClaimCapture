package controllers.s4_care_you_provide

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._

object G8OneWhoPaysPersonalDetails extends Controller with CareYouProvideRouting with CachedClaim {
  val form = Form(
    mapping(
      "organisation" -> optional(text(maxLength = hundred)),
      "title" -> optional(text(maxLength = 4)),
      "firstName" -> nonEmptyText(maxLength = Name.maxLength),
      "middleName" -> optional(text(maxLength = Name.maxLength)),
      "surname" -> nonEmptyText(maxLength = Name.maxLength),
      "amount" -> nonEmptyText.verifying(validDecimalNumber),
      "startDatePayment" -> dayMonthYear.verifying(validDate)
    )(OneWhoPaysPersonalDetails.apply)(OneWhoPaysPersonalDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val hasSomeonePaidYou: Boolean = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => m.hasSomeonePaidYou == yes
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