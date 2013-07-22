package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.{ FormError, Form }
import play.api.data.Forms._
import models.domain.{ Claim, AboutOtherMoney }
import controllers.Mappings._
import models.domain.MoreAboutYou
import models.yesNo.YesNoWith2Text
import utils.helpers.CarersForm._

object G1AboutOtherMoney extends Controller with CachedClaim {
  def yourBenefitsMapping(implicit claim: Claim) =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text1" -> optional(nonEmptyText(maxLength = fifty)),
      "text2" -> optional(nonEmptyText(maxLength = fifty))
    )(YesNoWith2Text.apply)(YesNoWith2Text.unapply)
      .verifying("text1.required", c => YesNoWith2Text.validateText(c, c.text1))
      .verifying("text2.required", c => YesNoWith2Text.validateText(c, c.text2, eitherClaimedBenefitSinceClaimDate))

  def form(implicit claim: Claim) = Form(
    mapping(
      yourBenefitsMapping,
      call(routes.G1AboutOtherMoney.present())
    )(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AboutOtherMoney)

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def eitherClaimedBenefitSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.eitherClaimedBenefitSinceClaimDate == yes
    case _ => false
  }

  def present = claiming { implicit claim =>
    implicit request =>
      OtherMoney.whenVisible(claim)(() => {
        val currentForm: Form[AboutOtherMoney] = claim.questionGroup(AboutOtherMoney) match {
          case Some(m: AboutOtherMoney) => form.fill(m)
          case _ => form
        }
        Ok(views.html.s8_other_money.g1_aboutOtherMoney(currentForm, completedQuestionGroups, hadPartnerSinceClaimDate, eitherClaimedBenefitSinceClaimDate))
      })
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("yourBenefits", "text1.required", FormError("yourBenefits.text1", "error.required"))
            .replaceError("yourBenefits", "text2.required", FormError("yourBenefits.text2", "error.required"))
          BadRequest(views.html.s8_other_money.g1_aboutOtherMoney(formWithErrorsUpdate, completedQuestionGroups, hadPartnerSinceClaimDate, eitherClaimedBenefitSinceClaimDate))
        },
        f => claim.update(f) -> Redirect(routes.G2MoneyPaidToSomeoneElseForYou.present()))
  }
}