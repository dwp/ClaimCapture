package controllers.circs.s2_report_changes

import models.domain.CircumstancesEmploymentNotStarted
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{Form, FormError}
import controllers.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import models.yesNo.{YesNoWithText, YesNo}

object G12EmploymentNotStarted extends Controller with CachedChangeOfCircs with Navigable {
  val payIntoPension =
    "doYouPayIntoPension" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(carersNonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("doYouPayIntoPension.text.required", YesNoWithText.validateOnYes _)

  val careCostsForThisWork =
    "doCareCostsForThisWork" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatCosts" -> optional(carersNonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("doCareCostsForThisWork.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" -> optional(text(maxLength = 20)),
    "whenExpectedToBePaidDate" -> optional(dayMonthYear.verifying(validDate)),
    "howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly),
    "usuallyPaidSameAmount" -> optional(text.verifying(validYesNo)),
    payIntoPension,
    careCostsForThisWork,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesEmploymentNotStarted.apply)(CircumstancesEmploymentNotStarted.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesEmploymentNotStarted) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g12_employmentNotStarted(form.fill(CircumstancesEmploymentNotStarted)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency","error.required",FormError("howOften","error.required"))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength"))
          .replaceError("doYouPayIntoPension","doYouPayIntoPension.text.required",FormError("doYouPayIntoPension.whatFor","error.required"))
          .replaceError("doCareCostsForThisWork","doCareCostsForThisWork.text.required",FormError("doCareCostsForThisWork.whatCosts","error.required"))

        BadRequest(views.html.circs.s2_report_changes.g12_employmentNotStarted(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
