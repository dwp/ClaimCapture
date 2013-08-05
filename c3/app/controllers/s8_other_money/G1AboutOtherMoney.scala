package controllers.s8_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, AboutOtherMoney}
import controllers.Mappings._
import models.domain.MoreAboutYou
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._
import play.api.data.FormError
import scala.Some
import play.api.i18n.Messages

object G1AboutOtherMoney extends Controller with CachedClaim {

  val yourBenefitsMapping =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "text" -> optional(nonEmptyText(maxLength = fifty))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnYes _)

  val form = Form(
    mapping(
      yourBenefitsMapping,
      call(routes.G1AboutOtherMoney.present())
    )(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AboutOtherMoney)

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s8_other_money.g1_aboutOtherMoney(form.fill(AboutOtherMoney), completedQuestionGroups, hadPartnerSinceClaimDate))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val yourBenefitsAnswerErrorMessage = Messages("yourBenefits.answer.label",
                                                      if (hadPartnerSinceClaimDate) "or your Partner/Spouse" else "",
                                                      claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))

        val formWithErrorsUpdate = formWithErrors
          .replaceError("yourBenefits.answer", FormError(yourBenefitsAnswerErrorMessage, "error.required"))
          .replaceError("yourBenefits", FormError("yourBenefits.text", "error.required"))
        BadRequest(views.html.s8_other_money.g1_aboutOtherMoney(formWithErrorsUpdate, completedQuestionGroups, hadPartnerSinceClaimDate))
      },
      f => claim.update(f) -> Redirect(routes.G2MoneyPaidToSomeoneElseForYou.present()))
  }
}