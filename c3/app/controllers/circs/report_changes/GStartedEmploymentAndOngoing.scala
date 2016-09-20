package controllers.circs.report_changes

import models.domain.{CircumstancesEmploymentChange, CircumstancesStartedEmploymentAndOngoing}
import play.api.Play._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{Form, FormError}
import controllers.mappings.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.i18n._


object GStartedEmploymentAndOngoing extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" -> required(nonEmptyText.verifying(validCurrency8Required)),
    "whatDatePaid" -> dayMonthYear.verifying(validDate),
    "howOften" -> mandatoryPaymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText(maxLength = 35)),
    "usuallyPaidSameAmount" -> nonEmptyText.verifying(validYesNo)
  )(CircumstancesStartedEmploymentAndOngoing.apply)(CircumstancesStartedEmploymentAndOngoing.unapply)
    .verifying("expected.monthlyPayDay", validateMonthlyPayDay _))

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesEmploymentChange) {
      implicit circs => Ok(views.html.circs.report_changes.startedEmploymentAndOngoing(form.fill(CircumstancesStartedEmploymentAndOngoing)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency",errorRequired,FormError("howOften",errorRequired))
          .replaceError("howOften.frequency.other",maxLengthError,FormError("howOften",maxLengthError))
          .replaceError("howOften.frequency.other","error.restricted.characters",FormError("howOften","error.restricted.characters"))
          .replaceError("", "expected.monthlyPayDay",FormError("monthlyPayDay",errorRequired))

        BadRequest(views.html.circs.report_changes.startedEmploymentAndOngoing(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(routes.GEmploymentPensionExpenses.present)
    )
  }

  private def validateMonthlyPayDay(input: CircumstancesStartedEmploymentAndOngoing): Boolean = input.howOften.frequency match {
    case "monthly" => input.monthlyPayDay.isDefined
    case _ => true
  }
}
