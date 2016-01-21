package controllers.s_other_money

import controllers.mappings.Mappings
import play.api.Play._

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import controllers.CarersForms._
import models.view.CachedClaim
import models.domain.{YourPartnerPersonalDetails, AboutOtherMoney, Claim}
import controllers.mappings.Mappings._
import controllers.mappings.AddressMappings._
import utils.helpers.CarersForm._
import models.view.Navigable
import play.api.data.FormError
import models.yesNo.{YesNo, YesNoWithEmployerAndMoney}
import play.api.i18n._

object GAboutOtherMoney extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val anyPaymentsSinceClaimDateMapping =
    "anyPaymentsSinceClaimDate" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
    )(YesNo.apply)(YesNo.unapply)

  val statutorySickPayMapping =
    "statutorySickPay" -> mapping(
      "answer" ->  nonEmptyText.verifying(validYesNo),
      "howMuch" -> optional(nonEmptyText verifying validCurrency8Required),
      "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
      "employersName" -> optional(carersNonEmptyText(maxLength = sixty)),
      "employersAddress" -> optional(address.verifying(requiredAddress)),
      "employersPostcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode))
    )(YesNoWithEmployerAndMoney.apply)(YesNoWithEmployerAndMoney.unapply)
      .verifying("statEmployerNameRequired", YesNoWithEmployerAndMoney.validateEmployerNameOnYes _)
      .verifying("statHowMuchRequired", YesNoWithEmployerAndMoney.validateHowMuchOnYes _)

  val otherStatutoryPayMapping =
    "otherStatutoryPay" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "howMuch" -> optional(nonEmptyText verifying validCurrency8Required),
      "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
      "employersName" -> optional(carersNonEmptyText(maxLength = sixty)),
      "employersAddress" -> optional(address.verifying(requiredAddress)),
      "employersPostcode" -> optional(text verifying(restrictedPostCodeAddressStringText, validPostcode))
    )(YesNoWithEmployerAndMoney.apply)(YesNoWithEmployerAndMoney.unapply)
      .verifying("otherPayEmployerNameRequired", YesNoWithEmployerAndMoney.validateEmployerNameOnYes _)
      .verifying("otherPayHowMuchRequired", YesNoWithEmployerAndMoney.validateHowMuchOnYes _)

  val form = Form(mapping(
    anyPaymentsSinceClaimDateMapping,
    "whoPaysYou" -> optional(carersNonEmptyText(maxLength = Name.maxLength)),
    "howMuch" -> optional(nonEmptyText verifying validCurrency8Required),
    "howOften" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
    statutorySickPayMapping,
    otherStatutoryPayMapping
  )(AboutOtherMoney.apply)(AboutOtherMoney.unapply)
    .verifying("howMuch.required", validateHowMuch _)
    .verifying("whoPaysYou.required", validateWhoPays _)
  )

  private def validateWhoPays(aboutOtherMoney: AboutOtherMoney) = {
    aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
      case `yes` => aboutOtherMoney.whoPaysYou.isDefined
      case _ => true
    }
  }

  private def validateHowMuch(aboutOtherMoney: AboutOtherMoney) = {
    aboutOtherMoney.anyPaymentsSinceClaimDate.answer match {
      case `yes` => aboutOtherMoney.howMuch.isDefined
      case _ => true
    }
  }

  private def hadPartnerSinceClaimDate(implicit claim: Claim): Boolean = claim.questionGroup(YourPartnerPersonalDetails) match {
    case Some(p: YourPartnerPersonalDetails) => p.hadPartnerSinceClaimDate == yes
    case _ => false
  }

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(AboutOtherMoney) { implicit claim => Ok(views.html.s_other_money.g_aboutOtherMoney(form.fill(AboutOtherMoney), hadPartnerSinceClaimDate)) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val claimDate: String = claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`)
        val yourBenefitsAnswerErrorParams = Seq(if (hadPartnerSinceClaimDate) messagesApi("orPartnerSpouse") else "", claimDate)
        val anyPaymentsErrorParams = Seq(claimDate)

        val formWithErrorsUpdate = formWithErrors
          .replaceError("yourBenefits.answer",Mappings.errorRequired, FormError("yourBenefits.answer",Mappings.errorRequired,yourBenefitsAnswerErrorParams))
          .replaceError("anyPaymentsSinceClaimDate.answer",Mappings.errorRequired, FormError("anyPaymentsSinceClaimDate.answer",Mappings.errorRequired,anyPaymentsErrorParams))
          .replaceError("", "whoPaysYou.required", FormError("whoPaysYou", Mappings.errorRequired))
          .replaceError("", "howMuch.required", FormError("howMuch", Mappings.errorRequired))
          .replaceError("howOften.frequency.other",Mappings.maxLengthError,FormError("howOften",Mappings.maxLengthError))
          .replaceError("statutorySickPay.answer",Mappings.errorRequired, FormError("statutorySickPay.answer",Mappings.errorRequired))
          .replaceError("otherStatutoryPay.answer",Mappings.errorRequired, FormError("otherStatutoryPay.answer",Mappings.errorRequired))
          .replaceError("statutorySickPay","statHowMuchRequired", FormError("statutorySickPay.howMuch", Mappings.errorRequired))
          .replaceError("statutorySickPay","statEmployerNameRequired", FormError("statutorySickPay.employersName", Mappings.errorRequired))
          .replaceError("otherStatutoryPay","otherPayHowMuchRequired", FormError("otherStatutoryPay.howMuch", Mappings.errorRequired))
          .replaceError("otherStatutoryPay","otherPayEmployerNameRequired", FormError("otherStatutoryPay.employersName", Mappings.errorRequired))

        BadRequest(views.html.s_other_money.g_aboutOtherMoney(formWithErrorsUpdate, hadPartnerSinceClaimDate))
      },
      aboutOtherMoney => claim.update(formatPostCodes(aboutOtherMoney)) -> Redirect(controllers.s_pay_details.routes.GHowWePayYou.present()))
  } withPreview()

  private def formatPostCodes(aboutOtherMoney : AboutOtherMoney) : AboutOtherMoney = {
    aboutOtherMoney.copy(
      statutorySickPay = aboutOtherMoney.statutorySickPay.copy(postCode = Some(formatPostCode(aboutOtherMoney.statutorySickPay.postCode.getOrElse("")))),
      otherStatutoryPay = aboutOtherMoney.otherStatutoryPay.copy(postCode = Some(formatPostCode(aboutOtherMoney.otherStatutoryPay.postCode.getOrElse(""))))
    )
  }
}
