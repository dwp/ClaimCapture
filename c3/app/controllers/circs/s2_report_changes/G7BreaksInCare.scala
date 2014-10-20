package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.yesNo.{YesNoWithDateAndQs, RadioWithText, YesNoWithDateTimeAndText, YesNoDontKnowWithDates}
import models.domain.{CircumstancesAddressChange, CircumstancesBreaksInCare}
import controllers.CarersForms._
import play.api.data.FormError
import utils.helpers.PastPresentLabelHelper._
import play.api.data.FormError
import controllers.Mappings

/**
 * Created by neddakaltcheva on 3/20/14.
 */
object G7BreaksInCare extends Controller with CachedChangeOfCircs with Navigable {
  val whereWasPersonMapping =
    "wherePersonBreaksInCare" -> mapping(
      "answer" -> carersNonEmptyText,
      "text" -> optional(carersText(maxLength = Mappings.sixty))
    )(RadioWithText.apply)(RadioWithText.unapply)
     .verifying("wherePersonBreaksInCare.text.required", RadioWithText.validateOnOther _)

  val whereWereYouMapping =
    "whereYouBreaksInCare" -> mapping(
      "answer" -> carersNonEmptyText,
      "text" -> optional(carersText(maxLength = Mappings.sixty))
    )(RadioWithText.apply)(RadioWithText.unapply)
    .verifying("whereYouBreaksInCare.text.required", RadioWithText.validateOnOther _)

  val breakEndedMapping =
    "breakEnded" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "endDate" -> optional(dayMonthYear verifying validDateOnly),
      "endTime" -> optional(text)
    )(YesNoWithDateTimeAndText.apply)(YesNoWithDateTimeAndText.unapply)
    .verifying("endDate", validateBreakEndedEndDate _)

  val expectStartCaringMapping =
  "expectStartCaring" -> mapping(
    "answer" -> optional(carersText), //YesNoDontKnow
    "expectStartCaringDate" -> optional(dayMonthYear verifying validDateOnly),
    "permanentBreakDate" -> optional(dayMonthYear verifying validDateOnly)
  )(YesNoDontKnowWithDates.apply)(YesNoDontKnowWithDates.unapply)
    .verifying("permanentBreakDate", validateStartCaring _)

  val form = Form(mapping(
    "breaksInCareStartDate" -> dayMonthYear.verifying(validDate),
    "breaksInCareStartTime" -> optional(text),
    whereWasPersonMapping,
    whereWereYouMapping,
    breakEndedMapping,
    expectStartCaringMapping,
    "medicalCareDuringBreak" -> (nonEmptyText verifying validYesNo),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesBreaksInCare.apply)(CircumstancesBreaksInCare.unapply)
    .verifying("expectStartCaring", validateBreakEnded _)
  )

  private def validateBreakEnded(form: CircumstancesBreaksInCare) = {
    form.breakEnded.answer match {
      case `no` => form.expectStartCaring.answer.isDefined
      case _ => true
    }
  }

  private def validateBreakEndedEndDate(breakEnded: YesNoWithDateTimeAndText) = {
    breakEnded.answer match {
      case `yes` => breakEnded.date.isDefined
      case _ => true
    }
  }

  private def validateStartCaring(expectStartCaring: YesNoDontKnowWithDates) = {
    expectStartCaring.answer match {
      case Some(n) =>  n match {
        case `no` => expectStartCaring.permanentBreakDate.isDefined
        case _ => true
      }
      case _ => true
    }
  }

  def present = claiming {implicit circs =>  implicit request =>  lang =>
    track(CircumstancesBreaksInCare) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g7_breaksInCare(form.fill(CircumstancesBreaksInCare))(lang))
    }
  }

  def submit = claiming {implicit circs =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("wherePersonBreaksInCare.answer","error.required",FormError("wherePersonBreaksInCare","error.required"))
          .replaceError("whereYouBreaksInCare.answer","error.required",FormError("whereYouBreaksInCare","error.required"))
          .replaceError("breakEnded","endDate", FormError("breakEnded.endDate", "error.required"))
          .replaceError("","expectStartCaring", FormError("expectStartCaring.answer", "error.required"))
          .replaceError("expectStartCaring","permanentBreakDate", FormError("expectStartCaring.permanentBreakDate", "error.required"))
          .replaceError("wherePersonBreaksInCare","wherePersonBreaksInCare.text.required", FormError("wherePersonBreaksInCare.text", "error.required"))
          .replaceError("whereYouBreaksInCare","whereYouBreaksInCare.text.required", FormError("whereYouBreaksInCare.text", "error.required"))
        BadRequest(views.html.circs.s2_report_changes.g7_breaksInCare(updatedFormWithErrors)(lang))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s2_report_changes.routes.G8BreaksInCareSummary.present())
    )
  }
}
