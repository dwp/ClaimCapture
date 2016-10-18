package controllers.circs.report_changes

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain.{CircumstancesEmploymentPay, CircumstancesEmploymentChange, CircumstancesStartedAndFinishedEmployment}
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.i18n._
import play.api.mvc.Controller
import utils.helpers.CarersForm._


object GEmploymentPay extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "paid" -> nonEmptyText.verifying(validYesNo),
    "howMuch" -> required(nonEmptyText.verifying(validCurrency8Required)),
    "payDate" -> dayMonthYear.verifying(validDate),
    "whatWasIncluded" -> optional(carersText(maxLength = 300)),
    "howOften" -> mandatoryPaymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText(maxLength = 35)),
    "usuallyPaidSameAmount" -> optional(nonEmptyText.verifying(validYesNo)),
    "owedMoney" -> nonEmptyText.verifying(validYesNo),
    "owedMoneyInfo" -> optional(carersText(maxLength = 300))
  )(CircumstancesEmploymentPay.apply)(CircumstancesEmploymentPay.unapply)
//    .verifying("expected.monthlyPayDay", validateMonthlyPayDay _)
//    .verifying("expected.employerOwesYouMoneyInfo", validateEmployerOwesYou _)
  )

  /*
        case `employed` => {
        employmentChange.hasWorkStartedYet.answer match {
          case `yes` => {
            if (employmentChange.hasWorkFinishedYet.answer.getOrElse("no") == `yes`) CircumstancesStartedAndFinishedEmployment -> controllers.circs.report_changes.routes.GStartedAndFinishedEmployment.present()
            else CircumstancesStartedEmploymentAndOngoing -> controllers.circs.report_changes.routes.GStartedEmploymentAndOngoing.present()
          }
          case _ => CircumstancesEmploymentNotStarted -> controllers.circs.report_changes.routes.GEmploymentNotStarted.present()
        }
      }
   */
  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesEmploymentChange) {
      implicit circs => Ok(views.html.circs.report_changes.EmploymentPay(form.fill(CircumstancesEmploymentPay)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency",errorRequired,FormError("howOften",errorRequired))
          .replaceError("howOften.frequency.other",maxLengthError,FormError("howOften",maxLengthError))
          .replaceError("", "expected.monthlyPayDay",FormError("monthlyPayDay",errorRequired))
          .replaceError("", "expected.employerOwesYouMoneyInfo",FormError("employerOwesYouMoneyInfo",errorRequired))
        BadRequest(views.html.circs.report_changes.EmploymentPay(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(routes.GEmploymentPensionExpenses.present)
    )
  }

  private def validateMonthlyPayDay(input: CircumstancesStartedAndFinishedEmployment): Boolean = input.howOften.frequency match {
    case "monthly" => input.monthlyPayDay.isDefined
    case _ => true
  }

  private def validateEmployerOwesYou(input: CircumstancesStartedAndFinishedEmployment): Boolean = input.employerOwesYouMoney match {
    case `yes` => input.employerOwesYouMoneyInfo.isDefined
    case _ => true
  }

}
