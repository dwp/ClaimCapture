package controllers.s8_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{PersonContactDetails, PersonWhoGetsThisMoney, Claim, MoneyPaidToSomeoneElseForYou}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2MoneyPaidToSomeoneElseForYou extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "moneyAddedToBenefitSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      call(routes.G2MoneyPaidToSomeoneElseForYou.present())
    )(MoneyPaidToSomeoneElseForYou.apply)(MoneyPaidToSomeoneElseForYou.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoneyPaidToSomeoneElseForYou)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s8_other_money.g2_moneyPaidToSomeoneElseForYou(form.fill(MoneyPaidToSomeoneElseForYou), completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_other_money.g2_moneyPaidToSomeoneElseForYou(formWithErrors, completedQuestionGroups)),
      f => {
        val deletePersonQuestionGroups = f.moneyAddedToBenefitSinceClaimDate != yes

        val updatedClaim = if (deletePersonQuestionGroups) claim.delete(PersonWhoGetsThisMoney).delete(PersonContactDetails) else claim

        updatedClaim.update(f) -> Redirect(routes.G3PersonWhoGetsThisMoney.present())
      })
  }
}