package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{PersonContactDetails, PersonWhoGetsThisMoney, MoneyPaidToSomeoneElseForYou}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2MoneyPaidToSomeoneElseForYou extends Controller with OtherMoneyRouting with CachedClaim {
  val form = Form(
    mapping(
      "moneyAddedToBenefitSinceClaimDate" -> nonEmptyText.verifying(validYesNo)
    )(MoneyPaidToSomeoneElseForYou.apply)(MoneyPaidToSomeoneElseForYou.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_other_money.g2_moneyPaidToSomeoneElseForYou(form.fill(MoneyPaidToSomeoneElseForYou), completedQuestionGroups(MoneyPaidToSomeoneElseForYou)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_other_money.g2_moneyPaidToSomeoneElseForYou(formWithErrors, completedQuestionGroups(MoneyPaidToSomeoneElseForYou))),
      f => {
        val deletePersonQuestionGroups = f.moneyAddedToBenefitSinceClaimDate != yes

        val updatedClaim = if (deletePersonQuestionGroups) claim.delete(PersonWhoGetsThisMoney).delete(PersonContactDetails) else claim

        updatedClaim.update(f) -> Redirect(routes.G3PersonWhoGetsThisMoney.present())
      })
  }
}