package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, AboutOtherMoney}
import controllers.Mappings._
import models.domain.MoreAboutYou
import models.yesNo.YesNoWith2Text
import utils.helpers.CarersForm._
import play.api.data.FormError
import play.api.i18n.Messages

object G1AboutOtherMoney extends Controller with CachedClaim {

  val yourBenefitsMapping =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text1" -> optional(nonEmptyText(maxLength = fifty)),
      "text2" -> optional(nonEmptyText(maxLength = fifty))
    )(YesNoWith2Text.apply)(YesNoWith2Text.unapply)
      .verifying("text1", YesNoWith2Text.validateText1OnYes _)
      .verifying("text2", YesNoWith2Text.validateText2OnYes _)

  val form = Form(
    mapping(
      yourBenefitsMapping
    )(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_other_money.g1_aboutOtherMoney(form.fill(AboutOtherMoney), hadPartnerSinceClaimDate))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val yourBenefitsAnswerErrorMessage = Messages("yourBenefits.answer.label",
                                                      if (hadPartnerSinceClaimDate) "or your Partner/Spouse" else "",
                                                      claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))

        val formWithErrorsUpdate = formWithErrors
          .replaceError("yourBenefits.answer", FormError(yourBenefitsAnswerErrorMessage, "error.required"))
          .replaceError("yourBenefits", "text1", FormError("yourBenefits.text1", "error.required"))
          .replaceError("yourBenefits", "text2", FormError("yourBenefits.text2", "error.required"))

        BadRequest(views.html.s9_other_money.g1_aboutOtherMoney(formWithErrorsUpdate, hadPartnerSinceClaimDate))
      },
      f => claim.update(f) -> Redirect(routes.G2MoneyPaidToSomeoneElseForYou.present()))
  }
}