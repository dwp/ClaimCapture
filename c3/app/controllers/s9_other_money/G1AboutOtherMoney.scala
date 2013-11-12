package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.FormError
import play.api.i18n.Messages
import models.view.CachedClaim
import models.domain.{StatutorySickPay, Claim, AboutOtherMoney, MoreAboutYou}
import controllers.Mappings._
import models.yesNo.YesNo
import utils.helpers.CarersForm._
import models.view.Navigable

object  G1AboutOtherMoney extends Controller with CachedClaim with Navigable {
  val yourBenefitsMapping =
    "yourBenefits" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply)
    
  val anyPaymentsSinceClaimDateMapping =
    "anyPaymentsSinceClaimDate" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply)

  val form = Form(mapping(
    yourBenefitsMapping,
    anyPaymentsSinceClaimDateMapping,
    "whoPaysYou" -> optional(nonEmptyText(maxLength = Name.maxLength)),
    "howMuch" -> optional(nonEmptyText verifying validDecimalNumber),
    "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly)
  )(AboutOtherMoney.apply)(AboutOtherMoney.unapply)
    .verifying("howMuch.required", validateHowMuch _)
    .verifying("whoPaysYou.required", validateWhoPays _)
  )

  def validateWhoPays(aboutOtherMoney: AboutOtherMoney) = {
    aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
      case `yes` => aboutOtherMoney.whoPaysYou.isDefined
      case _ => true
    }
  }

  def validateHowMuch(aboutOtherMoney: AboutOtherMoney) = {
    aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
      case `yes` => aboutOtherMoney.howMuch.isDefined
      case _ => true
    }
  }

  def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(MoreAboutYou) match {
    case Some(m: MoreAboutYou) => m.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def present = claiming { implicit claim => implicit request =>
    track(AboutOtherMoney) { implicit claim => Ok(views.html.s9_other_money.g1_aboutOtherMoney(form.fill(AboutOtherMoney), hadPartnerSinceClaimDate)) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val yourBenefitsAnswerErrorMessage = Messages("yourBenefits.answer.label",
                                                      if (hadPartnerSinceClaimDate) "or your Partner/Spouse" else "",
                                                      claim.dateOfClaim.fold("")(_.`dd/MM/yyyy`))

        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "howMuch.required", FormError("howMuch", "error.required"))
          .replaceError("", "whoPaysYou.required", FormError("whoPaysYou", "error.required"))
          .replaceError("yourBenefits.answer", FormError(yourBenefitsAnswerErrorMessage, "error.required"))

        BadRequest(views.html.s9_other_money.g1_aboutOtherMoney(formWithErrorsUpdate, hadPartnerSinceClaimDate))
      },
      f => claim.update(f) -> Redirect(routes.G5StatutorySickPay.present()))
  }
}