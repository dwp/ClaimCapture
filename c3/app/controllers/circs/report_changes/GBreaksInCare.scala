package controllers.circs.report_changes

import play.api.Play._
import play.api.mvc.Controller
import models.view.{CachedChangeOfCircs, Navigable}
import scala.language.postfixOps
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.yesNo.{ RadioWithText, YesNoWithDateTimeAndText, YesNoDontKnowWithDates}
import models.domain.CircumstancesBreaksInCare
import controllers.CarersForms._
import controllers.mappings.Mappings
import play.api.i18n._


/**
 * Created by neddakaltcheva on 3/20/14.
 */
object GBreaksInCare  extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
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
      "endTime" -> optional(carersText)
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
    "breaksInCareStartTime" -> optional(carersText),
    whereWasPersonMapping,
    whereWereYouMapping,
    breakEndedMapping,
    expectStartCaringMapping,
    "medicalCareDuringBreak" -> (nonEmptyText verifying validYesNo),
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesBreaksInCare.textMaxLength))
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

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    Ok(views.html.circs.report_changes.breaksInCare(form.fill(CircumstancesBreaksInCare)))
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors
          .replaceError("wherePersonBreaksInCare.answer",Mappings.errorRequired,FormError("wherePersonBreaksInCare",Mappings.errorRequired))
          .replaceError("whereYouBreaksInCare.answer",Mappings.errorRequired,FormError("whereYouBreaksInCare",Mappings.errorRequired))
          .replaceError("breakEnded","endDate", FormError("breakEnded.endDate", Mappings.errorRequired))
          .replaceError("","expectStartCaring", FormError("expectStartCaring.answer", Mappings.errorRequired))
          .replaceError("expectStartCaring","permanentBreakDate", FormError("expectStartCaring.permanentBreakDate", Mappings.errorRequired))
          .replaceError("wherePersonBreaksInCare","wherePersonBreaksInCare.text.required", FormError("wherePersonBreaksInCare.text", Mappings.errorRequired))
          .replaceError("whereYouBreaksInCare","whereYouBreaksInCare.text.required", FormError("whereYouBreaksInCare.text", Mappings.errorRequired))
        BadRequest(views.html.circs.report_changes.breaksInCare(updatedFormWithErrors))
      },
      f => circs.update(f) -> Redirect(controllers.circs.report_changes.routes.GBreaksInCareSummary.present())
    )
  }
}
