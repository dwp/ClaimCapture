package controllers.s9_other_money

import language.reflectiveCalls
import controllers.Mappings._
import models.domain.{MoneyPaidToSomeoneElseForYou, PersonWhoGetsThisMoney}
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding

object G3PersonWhoGetsThisMoney extends Controller with OtherMoneyRouting with CachedClaim {
  val form = Form(
    mapping(
      "fullName" -> nonEmptyText(maxLength = sixty),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
      "nameOfBenefit" -> nonEmptyText(maxLength = sixty)
    )(PersonWhoGetsThisMoney.apply)(PersonWhoGetsThisMoney.unapply))

  def present = claiming { implicit claim => implicit request =>
    val iAmVisible = claim.questionGroup(MoneyPaidToSomeoneElseForYou) match {
      case Some(t: MoneyPaidToSomeoneElseForYou) => t.moneyAddedToBenefitSinceClaimDate == yes
      case _ => true
    }

    if (iAmVisible)
      Ok(views.html.s9_other_money.g3_personWhoGetsThisMoney(form.fill(PersonWhoGetsThisMoney), completedQuestionGroups(PersonWhoGetsThisMoney)))
    else
      Redirect(routes.G4PersonContactDetails.present())
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_other_money.g3_personWhoGetsThisMoney(formWithErrors, completedQuestionGroups(PersonWhoGetsThisMoney))),
      f => claim.update(f) -> Redirect(routes.G4PersonContactDetails.present()))
  }
}