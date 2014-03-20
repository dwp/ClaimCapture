package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.yesNo.{RadioWithText, YesNoWithDateTimeAndText, YesNoDontKnowWithDates}
import models.domain.{CircumstancesAddressChange, CircumstancesBreaksInCare}
import controllers.CarersForms._
import play.api.data.FormError

/**
 * Created by neddakaltcheva on 3/20/14.
 */
object G7BreaksInCare extends Controller with CachedChangeOfCircs with Navigable {

  val whereWereYouMapping =
    "whereYou" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "somewhereElse" -> optional(text)
    )(RadioWithText.apply)(RadioWithText.unapply)

  val whereWasPersonMapping =
    "wherePerson" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "somewhereElse" -> optional(text)
    )(RadioWithText.apply)(RadioWithText.unapply)

  val breakEndedMapping =
    "breakEnded" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "endDate" -> optional(dayMonthYear verifying validDateOnly),
      "endTime" -> optional(text),
      "expectedStartCaring" -> optional(text) //YesNoDontKnow
    )(YesNoWithDateTimeAndText.apply)(YesNoWithDateTimeAndText.unapply)

  val expectStartCaringMapping =
  "expectStartCaring" -> mapping(
    "answer" -> optional(carersText()),
    "expectStartCaringDate" -> optional(dayMonthYear verifying validDateOnly),
    "permanentBreakDate" -> optional(dayMonthYear verifying validDateOnly)
  )(YesNoDontKnowWithDates.apply)(YesNoDontKnowWithDates.unapply)

  val form = Form(mapping(
    "startDate" -> (dayMonthYear verifying validDateOnly),
    "startTime" -> optional(text),
    whereWasPersonMapping,
    whereWereYouMapping,
    breakEndedMapping,
    expectStartCaringMapping,
    "medicalDuringBreak" -> (nonEmptyText verifying validYesNo),
    "moreAboutChanges" -> optional(text)
  )(CircumstancesBreaksInCare.apply)(CircumstancesBreaksInCare.unapply)
    .verifying("breakEnded.answer", validateBreakEnded _)
  )

  def validateBreakEnded(form: CircumstancesBreaksInCare) = {
    form.breakEnded.answer match {
      case `yes` => form.breakEnded.expectStartCaring.isDefined
      case _ => true
    }
  }

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesBreaksInCare) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g7_breaksInCare(form.fill(CircumstancesBreaksInCare)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("breakEnded","breakEnded.answer", FormError("breakEnded.expectedStartCaring", "error.required"))
        BadRequest(views.html.circs.s2_report_changes.g7_breaksInCare(updatedFormWithErrors))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
