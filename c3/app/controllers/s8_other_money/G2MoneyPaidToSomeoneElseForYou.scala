package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ Claim, MoneyPaidToSomeoneElseForYou }
import utils.helpers.CarersForm._
import controllers.Mappings._

object G2MoneyPaidToSomeoneElseForYou extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "moneyAddedToBenefitSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "call" -> ignored(routes.G2MoneyPaidToSomeoneElseForYou.present())
    )(MoneyPaidToSomeoneElseForYou.apply)(MoneyPaidToSomeoneElseForYou.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoneyPaidToSomeoneElseForYou)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[MoneyPaidToSomeoneElseForYou] = claim.questionGroup(MoneyPaidToSomeoneElseForYou) match {
      case Some(m: MoneyPaidToSomeoneElseForYou) => form.fill(m)
      case _ => form
    }
    Ok(views.html.s8_other_money.g2_moneyPaidToSomeoneElseForYou(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_other_money.g2_moneyPaidToSomeoneElseForYou(formWithErrors, completedQuestionGroups)),
      f => claim.update(f) -> Redirect(routes.G3PersonWhoGetsThisMoney.present()))
  }
}