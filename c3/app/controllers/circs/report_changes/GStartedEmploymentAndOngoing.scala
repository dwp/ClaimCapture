package controllers.circs.report_changes

import app.PaymentFrequency
import app.{PensionPaymentFrequency, PaymentFrequency}
import models.{PaymentFrequency, DayMonthYear}
import models.domain.{CircumstancesStartedEmploymentAndOngoing, CircumstancesEmploymentChange}
import org.joda.time.DateTime
import play.api.Play._
import play.api.data.validation._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{Form, FormError}
import controllers.mappings.Mappings._
import play.api.data.Forms._
import utils.CommonValidation._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.i18n._

import scala.util.{Failure, Success, Try}


object GStartedEmploymentAndOngoing extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" -> text,
    "whatDatePaid" -> dayMonthYear,
    "howOften" -> paymentFrequencyNoCheck,
    "monthlyPayDay" -> optional(text),
    "usuallyPaidSameAmount" -> optional(text)
  )(CircumstancesStartedEmploymentAndOngoing.apply)(CircumstancesStartedEmploymentAndOngoing.unapply)
    .verifying(requiredHowMuchPaid)
    .verifying(requiredWhatDatePaid)
    .verifying(requiredHowOftenFrequencyValid)
    .verifying(requiredHowOften)
    .verifying(requiredUsuallyPaidSameAmount)
  )

  def requiredHowMuchPaid: Constraint[CircumstancesStartedEmploymentAndOngoing] = Constraint[CircumstancesStartedEmploymentAndOngoing]("constraint.howMuchPaid") { circumstancesStartedEmploymentAndOngoing =>
    val decimalPattern = CURRENCY_REGEX.r
    circumstancesStartedEmploymentAndOngoing.howMuchPaid.isEmpty match {
      case true => Invalid(ValidationError("howMuchPaid.required"))
      case false => validCurrencyWithPattern(decimalPattern, circumstancesStartedEmploymentAndOngoing.howMuchPaid)
    }
  }

  def requiredWhatDatePaid: Constraint[CircumstancesStartedEmploymentAndOngoing] = Constraint[CircumstancesStartedEmploymentAndOngoing]("constraint.whatDatePaid") { circumstancesStartedEmploymentAndOngoing =>
    validateDateWithField(circumstancesStartedEmploymentAndOngoing.date, "whatDatePaid")
  }

  def requiredHowOften: Constraint[CircumstancesStartedEmploymentAndOngoing] = Constraint[CircumstancesStartedEmploymentAndOngoing]("constraint.howOften") { circumstancesStartedEmploymentAndOngoing =>
    circumstancesStartedEmploymentAndOngoing.howOften.frequency match {
      case frequency if frequency.isEmpty => Valid
      case frequency if frequency == PensionPaymentFrequency.Monthly => monthlyPaymentDateRequired(circumstancesStartedEmploymentAndOngoing)
      case frequency if frequency == PensionPaymentFrequency.Other => otherRequired(circumstancesStartedEmploymentAndOngoing)
      case _ => Valid
    }
  }

  def requiredHowOftenFrequencyValid: Constraint[CircumstancesStartedEmploymentAndOngoing] = Constraint[CircumstancesStartedEmploymentAndOngoing]("constraint.howOften") { circumstancesStartedEmploymentAndOngoing =>
    circumstancesStartedEmploymentAndOngoing.howOften.frequency match {
      case frequency if frequency.isEmpty => Invalid(ValidationError("howOften.frequency.required"))
      case frequency => checkValidFrequency(frequency, sixty, "howOften.frequency")
    }
  }

  def requiredUsuallyPaidSameAmount: Constraint[CircumstancesStartedEmploymentAndOngoing] = Constraint[CircumstancesStartedEmploymentAndOngoing]("constraint.howOften") { circumstancesStartedEmploymentAndOngoing =>
    circumstancesStartedEmploymentAndOngoing.howOften.frequency match {
      case frequency if frequency.isEmpty => Valid
      case frequency => validYesNoWithField(circumstancesStartedEmploymentAndOngoing.usuallyPaidSameAmount.getOrElse(""), "usuallyPaidSameAmount.required")
    }
  }

  def monthlyPaymentDateRequired(circumstancesStartedEmploymentAndOngoing: CircumstancesStartedEmploymentAndOngoing) = {
    circumstancesStartedEmploymentAndOngoing.monthlyPayDay match {
      case Some(day) => checkValidFrequency(day, 35, "monthlyPayDay")
      case _ => Invalid(ValidationError("monthlyPayDay.required"))
    }
  }

  def otherRequired(circumstancesStartedEmploymentAndOngoing: CircumstancesStartedEmploymentAndOngoing) = {
    circumstancesStartedEmploymentAndOngoing.howOften match {
      case pf => {
        Try(new PaymentFrequency(pf.frequency, pf.other)) match {
          case Success(p: PaymentFrequency) if p.frequency == app.PensionPaymentFrequency.Other && p.other.isEmpty => Invalid(ValidationError("howOften.frequency.other.required"))
          case Success(p: PaymentFrequency) => checkValidFrequency(p.other.getOrElse(""), sixty, "howOften.frequency.other")
          case Failure(_) => Invalid(ValidationError("howOften.frequency.other.invalid"))
        }
      }
    }
  }

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesEmploymentChange) {
      implicit circs => Ok(views.html.circs.report_changes.startedEmploymentAndOngoing(form.fill(CircumstancesStartedEmploymentAndOngoing)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency", errorRequired, FormError("howOften", errorRequired))
          .replaceError("howOften.frequency.other", maxLengthError, FormError("howOften", maxLengthError))
          .replaceError("howOften.frequency.other", "error.restricted.characters", FormError("howOften","error.restricted.characters"))
          .replaceError("", invalidDecimal, FormError("howMuchPaid", invalidDecimal))
          .replaceError("", "howMuchPaid.required", FormError("howMuchPaid", errorRequired))
          .replaceError("", "whatDatePaid.date.required", FormError("whatDatePaid", errorRequired))
          .replaceError("", "whatDatePaid.date.invalid", FormError("whatDatePaid", errorInvalid))
          .replaceError("", "howOften.frequency.required", FormError("howOften", errorRequired))
          .replaceError("", "howOften.frequency.maxlength", FormError("howOften", maxLengthError))
          .replaceError("", "howOften.frequency.restricted.characters", FormError("howOften", errorRestrictedCharacters))
          .replaceError("", "howOften.frequency.other.required", FormError("howOften", errorRequired))
          .replaceError("", "howOften.frequency.other.invalid", FormError("howOften", errorInvalid))
          .replaceError("", "howOften.frequency.other.maxlength", FormError("howOften", maxLengthError))
          .replaceError("", "howOften.frequency.other.restricted.characters", FormError("howOften", errorRestrictedCharacters))
          .replaceError("", "usuallyPaidSameAmount.required", FormError("usuallyPaidSameAmount", errorRequired))
          .replaceError("", "monthlyPayDay.maxlength", FormError("monthlyPayDay", maxLengthError))
          .replaceError("", "monthlyPayDay.restricted.characters", FormError("monthlyPayDay", errorRestrictedCharacters))
          .replaceError("", "monthlyPayDay.required", FormError("monthlyPayDay", errorRequired))
          .replaceError("", "monthlyPayDay.invalid", FormError("monthlyPayDay", errorInvalid))

        BadRequest(views.html.circs.report_changes.startedEmploymentAndOngoing(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(routes.GEmploymentPensionExpenses.present)
    )
  }
}
