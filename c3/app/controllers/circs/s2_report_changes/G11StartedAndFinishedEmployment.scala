package controllers.circs.s2_report_changes

import models.domain.CircumstancesStartedAndFinishedEmployment
import play.api.Play._
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import controllers.mappings.Mappings._
import controllers.CarersForms._
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.i18n._


object G11StartedAndFinishedEmployment extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val payIntoPension =
    "didYouPayIntoPension" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(carersNonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("didYouPayIntoPension.text.required", YesNoWithText.validateOnYes _)

  val payForThings =
    "didYouPayForThings" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("didYouPayForThings.text.required", YesNoWithText.validateOnYes _)

  val careCostsForThisWork =
    "didCareCostsForThisWork" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatCosts" -> optional(carersNonEmptyText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("didCareCostsForThisWork.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" -> required(nonEmptyText.verifying(validCurrency8Required)),
    "dateLastPaid" -> dayMonthYear.verifying(validDate),
    "whatWasIncluded" -> optional(carersText(maxLength = 300)),
    "howOften" -> mandatoryPaymentFrequency.verifying(validPaymentFrequencyOnly),
    "monthlyPayDay" -> optional(carersText(maxLength = 35)),
    "usuallyPaidSameAmount" -> nonEmptyText.verifying(validYesNo),
    "employerOwesYouMoney" -> nonEmptyText.verifying(validYesNo),
    "employerOwesYouMoneyInfo" -> optional(carersText(maxLength = 300)),
    payIntoPension,
    payForThings,
    careCostsForThisWork,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesStartedAndFinishedEmployment.apply)(CircumstancesStartedAndFinishedEmployment.unapply)
    .verifying("expected.monthlyPayDay", validateMonthlyPayDay _)
    .verifying("expected.employerOwesYouMoneyInfo", validateEmployerOwesYou _)
  )

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesStartedAndFinishedEmployment) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g11_startedAndFinishedEmployment(form.fill(CircumstancesStartedAndFinishedEmployment)))
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
          .replaceError("didYouPayIntoPension","didYouPayIntoPension.text.required",FormError("didYouPayIntoPension.whatFor",errorRequired))
          .replaceError("didYouPayForThings","didYouPayForThings.text.required",FormError("didYouPayForThings.whatFor",errorRequired))
          .replaceError("didCareCostsForThisWork","didCareCostsForThisWork.text.required",FormError("didCareCostsForThisWork.whatCosts",errorRequired))

        BadRequest(views.html.circs.s2_report_changes.g11_startedAndFinishedEmployment(formWithErrorsUpdate))
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
