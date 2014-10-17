package controllers.circs.s2_report_changes

import models.domain.{CircumstancesStartedAndFinishedEmployment, CircumstancesEmploymentNotStarted}
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{Form, FormError}
import controllers.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import models.yesNo.{YesNoWithText, YesNo}
import models.PensionPaymentFrequency

object G12EmploymentNotStarted extends Controller with CachedChangeOfCircs with Navigable {
  val payIntoPension =
    "doYouPayIntoPension" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(nonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("doYouPayIntoPension.text.required", YesNoWithText.validateOnYes _)

  val careCostsForThisWork =
    "doCareCostsForThisWork" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatCosts" -> optional(nonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("doCareCostsForThisWork.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" ->  optional(nonEmptyText.verifying(validCurrency8Required)),
    "whenExpectedToBePaidDate" -> optional(dayMonthYear.verifying(validDate)),
    "howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly),
    "usuallyPaidSameAmount" -> optional(text.verifying(validYesNo)),
    payIntoPension,
    careCostsForThisWork,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesEmploymentNotStarted.apply)(CircumstancesEmploymentNotStarted.unapply)
    .verifying("expected.howMuchPaid",validateHowMuchPaid _)
    .verifying("expected.whenExpectedToBePaidDate",validateWhenExpectedToBePaid _)
    .verifying("expected.howOften",validateHowOften _)
    .verifying("expected.usuallyPaidSameAmount",validateUsuallyPaidSameAmount _)
  )

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesEmploymentNotStarted) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g12_employmentNotStarted(form.fill(CircumstancesEmploymentNotStarted)))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency","error.required",FormError("howOften","error.required"))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength"))
          .replaceError("doYouPayIntoPension","doYouPayIntoPension.text.required",FormError("doYouPayIntoPension.whatFor","error.required"))
          .replaceError("doCareCostsForThisWork","doCareCostsForThisWork.text.required",FormError("doCareCostsForThisWork.whatCosts","error.required"))
          .replaceError("", "expected.howMuchPaid",FormError("howMuchPaid","error.required"))
          .replaceError("", "expected.whenExpectedToBePaidDate",FormError("whenExpectedToBePaidDate","error.required"))
          .replaceError("", "expected.howOften",FormError("howOften","error.required"))
          .replaceError("", "expected.usuallyPaidSameAmount",FormError("usuallyPaidSameAmount","error.required"))

        BadRequest(views.html.circs.s2_report_changes.g12_employmentNotStarted(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }

  def validateHowMuchPaid(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howMuchPaid.isDefined
    case _ => true
  }

  def validateWhenExpectedToBePaid(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.whenExpectedToBePaidDate.isDefined
    case _ => true
  }

  def validateHowOften(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howOften.frequency.length > 0
    case _ => true
  }

  def validateUsuallyPaidSameAmount(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howOften.frequency match{
      case app.PensionPaymentFrequency.Other => true
      case _ if input.howOften.frequency.size > 0 => input.usuallyPaidSameAmount.isDefined
      case _ => true
    }
    case _ => true
  }
}
