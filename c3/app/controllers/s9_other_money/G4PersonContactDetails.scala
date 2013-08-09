package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.{MoneyPaidToSomeoneElseForYou, PersonContactDetails}
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G4PersonContactDetails extends Controller with OtherMoneyRouting with CachedClaim {
  val form = Form(
    mapping(
      "address" -> optional(address),
      "postcode" -> optional(text verifying validPostcode)
    )(PersonContactDetails.apply)(PersonContactDetails.unapply))

  def present = claiming { implicit claim => implicit request =>
    val iAmVisible = claim.questionGroup(MoneyPaidToSomeoneElseForYou) match {
      case Some(t: MoneyPaidToSomeoneElseForYou) => t.moneyAddedToBenefitSinceClaimDate == yes
      case _ => true
    }

    if (iAmVisible)
      Ok(views.html.s9_other_money.g4_personContactDetails(form.fill(PersonContactDetails), completedQuestionGroups(PersonContactDetails)))
    else
      Redirect(routes.G5StatutorySickPay.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_other_money.g4_personContactDetails(formWithErrors, completedQuestionGroups(PersonContactDetails))),
      f => claim.update(f) -> Redirect(routes.G5StatutorySickPay.present()))
  }
}