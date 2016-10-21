package controllers.circs.report_changes

import app.PensionPaymentFrequency
import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.PaymentFrequency
import models.domain._
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import scala.util.{Failure, Success, Try}


object GEmploymentPay extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "pastpresentfuture" -> text,
    "paid" -> optional(text.verifying(validYesNo)),
    "howmuch" -> optional(text.verifying(validCurrency8Required)),
    "paydate" -> optional(dayMonthYear.verifying(validDate)),
    "whatWasIncluded" -> optional(carersText(maxLength = 300)),
    "howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText(maxLength = 35)),
    "sameAmount" -> optional(nonEmptyText.verifying(validYesNo)),
    "owedMoney" -> optional(text.verifying(validYesNo)),
    "owedMoneyInfo" -> optional(carersText(maxLength = 300))
  )(CircumstancesEmploymentPay.apply)(CircumstancesEmploymentPay.unapply)
    .verifying("paid.required", validatePaid _)
    .verifying("howmuch.required", validateHowMuch _)
    .verifying("paydate.required", validatePayDate _)
    .verifying(requiredHowOftenFrequencyValid)
    .verifying(requiredHowOften)
    .verifying("sameAmount.required", validateSameAmount _)
    .verifying("owedMoney.required", validateEmployerOwesYouAnswer _)
    .verifying("owedMoneyInfo.required", validateEmployerOwesYouText _)
  )

  private def validatePaid(circsPay: CircumstancesEmploymentPay) = circsPay.paid match {
    case Some(_) => true
    case _ => false
  }

  private def validateHowMuch(circsPay: CircumstancesEmploymentPay) = (circsPay.pastpresentfuture, circsPay.paid, circsPay.howMuch) match {
    case ("future", Some(Mappings.no), _) => true
    case (_, _, Some(s)) => true
    case (_, None, _) => true
    case _ => false
  }

  private def validatePayDate(circsPay: CircumstancesEmploymentPay) = (circsPay.pastpresentfuture, circsPay.paid, circsPay.payDate) match {
    case ("future", Some(Mappings.no), _) => true
    case (_, _, Some(s)) => true
    case (_, None, _) => true
    case _ => false
  }

  private def validateSameAmount(circsPay: CircumstancesEmploymentPay) = {
    circsPay.howOften.frequency match {
      case frequency if frequency.isEmpty => true
      case _ => circsPay.sameAmount.isDefined
    }
  }

  def requiredHowOften: Constraint[CircumstancesEmploymentPay] = Constraint[CircumstancesEmploymentPay]("constraint.howOften") { circsPay =>
   (circsPay.pastpresentfuture, circsPay.paid, circsPay.howOften.frequency) match {
      case ("future", Some(Mappings.no), _) => Valid
      case (_, None, _) => Valid
      case (_, _, frequency) if frequency.isEmpty => Valid
      case (_, _, frequency) if frequency == PensionPaymentFrequency.Monthly => monthlyPaymentDateRequired(circsPay)
      case (_, _, frequency) if frequency == PensionPaymentFrequency.Other => otherRequired(circsPay)
      case _ => Valid
    }
  }

  def requiredHowOftenFrequencyValid: Constraint[CircumstancesEmploymentPay] = Constraint[CircumstancesEmploymentPay]("constraint.howOften") { circsPay =>
      (circsPay.pastpresentfuture, circsPay.paid, circsPay.howOften.frequency) match {
        case ("future", Some(Mappings.no), _) => Valid
        case (_, None, _) => Valid
        case (_, _, frequency) if frequency.isEmpty => Invalid(ValidationError("howOften.frequency.required"))
        case (_, _, frequency) => checkValidFrequency(frequency, sixty, "howOften.frequency")
      }
  }

  def monthlyPaymentDateRequired(circsPay: CircumstancesEmploymentPay) = {
    circsPay.monthlyPayDay match {
      case Some(day) => checkValidFrequency(day, 35, "monthlyPayDay")
      case _ => Invalid(ValidationError("monthlyPayDay.required"))
    }
  }

  def otherRequired(circsPay: CircumstancesEmploymentPay) = {
    circsPay.howOften match {
      case pf => {
        Try(new PaymentFrequency(pf.frequency, pf.other)) match {
          case Success(p: PaymentFrequency) if p.frequency == app.PensionPaymentFrequency.Other && p.other.isEmpty => Invalid(ValidationError("howOften.frequency.other.required"))
          case Success(p: PaymentFrequency) => checkValidFrequency(p.other.getOrElse(""), sixty, "howOften.frequency.other")
          case Failure(_) => Invalid(ValidationError("howOften.frequency.other.invalid"))
        }
      }
    }
  }

  private def validateEmployerOwesYouAnswer(input: CircumstancesEmploymentPay): Boolean = input.pastpresentfuture match {
    case "past" => input.owedMoney.isDefined
    case _ => true
  }

  private def validateEmployerOwesYouText(input: CircumstancesEmploymentPay): Boolean = input.owedMoney match {
    case Some(Mappings.yes) => input.owedMoneyInfo.isDefined
    case _ => true
  }


  def present = claiming {
    implicit circs => implicit request => implicit request2lang =>
      track(CircumstancesEmploymentChange) {
        implicit circs => Ok(views.html.circs.report_changes.EmploymentPay(form.fill(CircumstancesEmploymentPay)))
      }
  }

  def submit = claiming {
    implicit circs => implicit request => implicit request2lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          // Check if the browser is jsenabled so we can use generic labels in error messages
          // check the selected paid button in the posted data not in the claim and only if js is enabled
          val jsEnabled = formWithErrors.data.getOrElse("jsEnabled", false)
          val pastpresentfuture = formWithErrors.data.getOrElse("pastpresentfuture", "error-past-present-future")
          val paidYesNo = formWithErrors.data.getOrElse("paid", "")
          val paidNoExpectString = (jsEnabled, paidYesNo) match {
            case ("true", Mappings.no) => ".expect"
            case _ => ""
          }

          // Note that this hacky error replacement requires none tense error message like pension={0} and then actual error-message of pension.past=The errror message.
          def errorMsgWithTense(errorKey: String) = {
            val expect = paidNoExpectString
            val msgkey = s"$errorKey$expect.$pastpresentfuture"
            Seq(messagesApi(msgkey))
          }

          val formWithErrorsUpdate = formWithErrors
            .replaceError("", "paid.required", FormError("paid", errorRequired, errorMsgWithTense("paid")))
            .replaceError("", "howmuch.required", FormError("howmuch", errorRequired, errorMsgWithTense("howmuch")))
            .replaceError("howmuch", "decimal.invalid", FormError("howmuch", errorInvalid, errorMsgWithTense("howmuch")))
            .replaceError("", "paydate.required", FormError("paydate", errorRequired, errorMsgWithTense("paydate")))
            .replaceError("paydate", "error.invalid", FormError("paydate", errorInvalid, errorMsgWithTense("paydate")))
            .replaceError("", "howOften.frequency.required", FormError("howOften", errorRequired, errorMsgWithTense("howOften")))
            .replaceError("", "howOften.frequency.maxlength", FormError("howOften", maxLengthError, errorMsgWithTense("howOften")))
            .replaceError("", "howOften.frequency.restricted.characters", FormError("howOften", errorRestrictedCharacters, errorMsgWithTense("howOften")))
            .replaceError("howOften", "error.paymentFrequency", FormError("howOften", errorRequired, errorMsgWithTense("howOften")))
            .replaceError("howOften", "error.frequency.other.invalid", FormError("howOften", errorInvalid, errorMsgWithTense("howOften")))
            .replaceError("howOften", "error.frequency.other.maxlength", FormError("howOften", maxLengthError, errorMsgWithTense("howOften")))
            .replaceError("howOften.frequency.other", "error.restricted.characters", FormError("howOften", errorRestrictedCharacters, errorMsgWithTense("howOften")))
            .replaceError("", "sameAmount.required", FormError("sameAmount", errorRequired, errorMsgWithTense("sameAmount.other")))
            .replaceError("monthlyPayDay", "error.maxlength", FormError("monthlyPayDay", maxLengthError, errorMsgWithTense("monthlyPayDay")))
            .replaceError("monthlyPayDay", "error.restricted.characters", FormError("monthlyPayDay", errorRestrictedCharacters, errorMsgWithTense("monthlyPayDay")))
            .replaceError("", "monthlyPayDay.required", FormError("monthlyPayDay", errorRequired, errorMsgWithTense("monthlyPayDay")))
            .replaceError("monthlyPayDay", "error.invalid", FormError("monthlyPayDay", errorInvalid, errorMsgWithTense("monthlyPayDay")))
            .replaceError("", "owedMoney.required", FormError("owedMoney", errorRequired, "owedMoney"))
            .replaceError("", "owedMoneyInfo.required", FormError("owedMoneyInfo", errorRequired, "owedMoneyInfo"))
          BadRequest(views.html.circs.report_changes.EmploymentPay(formWithErrorsUpdate))
        },
        f => circs.update(f) -> Redirect(routes.GEmploymentPensionExpenses.present)
      )
  }
}
