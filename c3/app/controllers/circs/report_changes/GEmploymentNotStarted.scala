package controllers.circs.report_changes

import models.domain.CircumstancesEmploymentNotStarted
import play.api.Play._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{Form, FormError}
import controllers.mappings.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.CarersForms._
import models.yesNo.YesNoWithText
import play.api.i18n._


object GEmploymentNotStarted extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val payIntoPension =
    "willYouPayIntoPension" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(carersText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("willYouPayIntoPension.text.required", YesNoWithText.validateOnYes _)

  val payForThings =
    "willYouPayForThings" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatFor" -> optional(carersText(minLength=1, maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("willYouPayForThings.text.required", YesNoWithText.validateOnYes _)

  val careCostsForThisWork =
    "willCareCostsForThisWork" -> mapping (
      "answer" -> nonEmptyText.verifying(validYesNo),
      "whatCosts" -> optional(carersText(maxLength = 300))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("willCareCostsForThisWork.text.required", YesNoWithText.validateOnYes _)

  val form = Form(mapping(
    "beenPaidYet" -> nonEmptyText.verifying(validYesNo),
    "howMuchPaid" ->  optional(nonEmptyText.verifying(validCurrency8Required)),
    "whenExpectedToBePaidDate" -> optional(dayMonthYear.verifying(validDate)),
    "howOften" -> paymentFrequency.verifying(validPaymentFrequencyOnly),
    "usuallyPaidSameAmount" -> optional(text.verifying(validYesNo)),
    payIntoPension,
    payForThings,
    careCostsForThisWork,
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesEmploymentNotStarted.apply)(CircumstancesEmploymentNotStarted.unapply)
    .verifying("expected.howMuchPaid",validateHowMuchPaid _)
    .verifying("expected.whenExpectedToBePaidDate",validateWhenExpectedToBePaid _)
    .verifying("expected.howOften",validateHowOften _)
    .verifying("expected.usuallyPaidSameAmount",validateUsuallyPaidSameAmount _)
  )

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesEmploymentNotStarted) {
      implicit circs => Ok(views.html.circs.report_changes.employmentNotStarted(form.fill(CircumstancesEmploymentNotStarted)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("howOften.frequency",errorRequired,FormError("howOften",errorRequired))
          .replaceError("howOften.frequency.other",maxLengthError,FormError("howOften",maxLengthError))
          .replaceError("willYouPayIntoPension","willYouPayIntoPension.text.required",FormError("willYouPayIntoPension.whatFor",errorRequired))
          .replaceError("willYouPayForThings","willYouPayForThings.text.required",FormError("willYouPayForThings.whatFor",errorRequired))
          .replaceError("willCareCostsForThisWork","willCareCostsForThisWork.text.required",FormError("willCareCostsForThisWork.whatCosts",errorRequired))
          .replaceError("", "expected.howMuchPaid",FormError("howMuchPaid",errorRequired))
          .replaceError("", "expected.whenExpectedToBePaidDate",FormError("whenExpectedToBePaidDate",errorRequired))
          .replaceError("", "expected.howOften",FormError("howOften",errorRequired))
          .replaceError("", "expected.usuallyPaidSameAmount",FormError("usuallyPaidSameAmount",errorRequired))

        BadRequest(views.html.circs.report_changes.employmentNotStarted(formWithErrorsUpdate))
      },
      f => circs.update(f) -> Redirect(controllers.circs.consent_and_declaration.routes.GCircsDeclaration.present())
    )
  }

  private def validateHowMuchPaid(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howMuchPaid.isDefined
    case _ => true
  }

  private def validateWhenExpectedToBePaid(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.whenExpectedToBePaidDate.isDefined
    case _ => true
  }

  private def validateHowOften(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howOften.frequency.length > 0
    case _ => true
  }

  private def validateUsuallyPaidSameAmount(input: CircumstancesEmploymentNotStarted): Boolean = input.beenPaid match {
    case `yes` => input.howOften.frequency match{
      case app.StatutoryPaymentFrequency.Other => true
      case app.StatutoryPaymentFrequency.DontKnowYet => true
      case _ if input.howOften.frequency.size > 0 => input.usuallyPaidSameAmount.isDefined
      case _ => true
    }
    case _ => true
  }
}
