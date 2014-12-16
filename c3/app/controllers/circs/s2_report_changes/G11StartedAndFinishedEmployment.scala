package controllers.circs.s2_report_changes

import models.domain.CircumstancesStartedAndFinishedEmployment
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import controllers.mappings.Mappings._
import controllers.CarersForms._
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}

object G11StartedAndFinishedEmployment extends Controller with CachedChangeOfCircs with Navigable {
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
    "howMuchPaid" -> required(nonEmptyText.verifying(validCurrency8Required)),
    "dateLastPaid" -> dayMonthYear.verifying(validDate),
    "whatWasIncluded" -> optional(carersText(maxLength = 60)),
    "howOften" -> mandatoryPaymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText(maxLength = 35)),
    "usuallyPaidSameAmount" -> nonEmptyText.verifying(validYesNo),
    "employerOwesYouMoney" -> nonEmptyText.verifying(validYesNo),
    "employerOwesYouMoneyInfo" -> optional(carersText(maxLength = 60)),
    payIntoPension,
    careCostsForThisWork,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesStartedAndFinishedEmployment.apply)(CircumstancesStartedAndFinishedEmployment.unapply)
    .verifying("expected.monthlyPayDay", validateMonthlyPayDay _)
    .verifying("expected.employerOwesYouMoneyInfo", validateEmployerOwesYou _)
  )

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesStartedAndFinishedEmployment) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g11_startedAndFinishedEmployment(form.fill(CircumstancesStartedAndFinishedEmployment))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency","error.required",FormError("howOften","error.required"))
          .replaceError("howOften.frequency.other","error.maxLength",FormError("howOften","error.maxLength"))
          .replaceError("", "expected.monthlyPayDay",FormError("monthlyPayDay","error.required"))
          .replaceError("", "expected.employerOwesYouMoneyInfo",FormError("employerOwesYouMoneyInfo","error.required"))
          .replaceError("doYouPayIntoPension","doYouPayIntoPension.text.required",FormError("doYouPayIntoPension.whatFor","error.required"))
          .replaceError("doCareCostsForThisWork","doCareCostsForThisWork.text.required",FormError("doCareCostsForThisWork.whatCosts","error.required"))

        BadRequest(views.html.circs.s2_report_changes.g11_startedAndFinishedEmployment(formWithErrorsUpdate)(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
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
