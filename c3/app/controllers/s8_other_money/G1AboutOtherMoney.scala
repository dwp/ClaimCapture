package controllers.s8_other_money

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ Claim, AboutOtherMoney }
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.MoreAboutYou
import models.yesNo.YesNoWith2Text

object G1AboutOtherMoney extends Controller with Routing with CachedClaim {
  override val route = AboutOtherMoney.id -> routes.G1AboutOtherMoney.present

  def yourBenefitsMapping(implicit claim: Claim) =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text1" -> optional(nonEmptyText(maxLength = fifty)),
      "text2" -> optional(nonEmptyText(maxLength = fifty)))(YesNoWith2Text.apply)(YesNoWith2Text.unapply)
      .verifying("required", YesNoWith2Text.validateOnYes(_, text1Enabled = true, text2Enabled = eitherClaimedBenefitSinceClaimDate))

  def form(implicit claim: Claim) = Form(
    mapping(
      yourBenefitsMapping)(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AboutOtherMoney)

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == "yes"
    case _ => false
  }
  def eitherClaimedBenefitSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.eitherClaimedBenefitSinceClaimDate == "yes"
    case _ => false
  }

  def present = claiming {
    implicit claim =>
      implicit request =>
        val currentForm: Form[AboutOtherMoney] = claim.questionGroup(AboutOtherMoney) match {
          case Some(m: AboutOtherMoney) => form.fill(m)
          case _ => form
        }
        Ok(views.html.s8_other_money.g1_aboutOtherMoney(currentForm, completedQuestionGroups, hadPartnerSinceClaimDate, eitherClaimedBenefitSinceClaimDate))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s8_other_money.g1_aboutOtherMoney(formWithErrors, completedQuestionGroups, hadPartnerSinceClaimDate, eitherClaimedBenefitSinceClaimDate)),
        f => claim.update(f) -> Redirect(controllers.routes.ThankYou.present())) // TODO replace with next page to go to
  }
}