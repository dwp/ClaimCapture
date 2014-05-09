package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import models.domain.CircumstancesStartedEmploymentAndOngoing
import play.api.data.{Form, FormError}
import controllers.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object G10StartedEmploymentAndOngoing extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "howMuchPaid" -> nonEmptyText(maxLength = 20),
    "haventBeenPaidYet" -> optional(carersText),
    "whatDatePaid" -> dayMonthYear.verifying(validDate),
    "howOften" -> mandatoryPaymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText),
    "usuallyPaidSameAmount" -> nonEmptyText.verifying(validYesNo)
  )(CircumstancesStartedEmploymentAndOngoing.apply)(CircumstancesStartedEmploymentAndOngoing.unapply)
    .verifying("expected.monthlyPayDay", validateMonthlyPayDay _))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesStartedEmploymentAndOngoing) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g10_startedEmploymentAndOngoing(form.fill(CircumstancesStartedEmploymentAndOngoing)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency","error.required",FormError("howOften","error.required"))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength"))
          .replaceError("", "expected.monthlyPayDay",FormError("monthlyPayDay","error.required"))

        BadRequest(views.html.circs.s2_report_changes.g10_startedEmploymentAndOngoing(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }

  def validateMonthlyPayDay(input: CircumstancesStartedEmploymentAndOngoing): Boolean = input.howOften.frequency match {
    case "monthly" => input.monthlyPayDay.isDefined
    case _ => true
  }
}
