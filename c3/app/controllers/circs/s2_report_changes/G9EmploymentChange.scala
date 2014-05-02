package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.CircumstancesEmploymentChange
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.yesNo._
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import models.DayMonthYear
import models.yesNo.YesNo
import play.api.data.FormError
import play.api.data.validation.ValidationError

object G9EmploymentChange extends Controller with CachedChangeOfCircs with Navigable {
  val employed = "employed"
  val selfemployed = "self-employed"

  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("dateRequired", YesNoWithDate.validateNo _)

  val hasWorkFinishedYet  =
    "hasWorkFinishedYet" -> mapping(
      "answer" -> optional(nonEmptyText.verifying(validYesNo)),
      "dateWhenFinished" -> optional(dayMonthYear.verifying(validDate))
    )(OptYesNoWithDate.apply)(OptYesNoWithDate.unapply)
      .verifying("expected.yesValue", OptYesNoWithDate.validateOnYes _)

  val hasWorkStartedYet =
    "hasWorkStartedYet" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "dateWhenStarted" -> optional(dayMonthYear.verifying(validDate)),
      hasWorkFinishedYet,
      "dateWhenWillStart" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDateAndOptYesNoWithDateOrDate.apply)(YesNoWithDateAndOptYesNoWithDateOrDate.unapply)
      .verifying("expected.yesDateValue", YesNoWithDateAndOptYesNoWithDateOrDate.validateDateOnYes _)
      .verifying("expected.yesYesNoValue", YesNoWithDateAndOptYesNoWithDateOrDate.validateYesNoOnYes _)

  val typeOfWork =
    "typeOfWork" -> mapping(
      "answer" -> nonEmptyText.verifying(validTypeOfWork)
    )(YesNo.apply)(YesNo.unapply)

  val form = Form(mapping(
    stillCaringMapping,
    hasWorkStartedYet,
    typeOfWork
  )(CircumstancesEmploymentChange.apply)(CircumstancesEmploymentChange.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesEmploymentChange) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g9_employmentChange(form.fill(CircumstancesEmploymentChange)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        println(formWithErrors)
        val updatedFormWithErrors = formWithErrors
          .replaceError("stillCaring","dateRequired", FormError("stillCaring.date", "error.required"))
          .replaceError("hasWorkStartedYet","expected.yesDateValue", FormError("hasWorkStartedYet.dateWhenStarted", "error.required"))
          .replaceError("hasWorkStartedYet","expected.yesYesNoValue", FormError("hasWorkStartedYet.hasWorkFinishedYet.answer", "error.required"))
          .replaceError("hasWorkStartedYet.hasWorkFinishedYet","expected.yesValue", FormError("hasWorkStartedYet.hasWorkFinishedYet.dateWhenFinished", "error.required"))
        BadRequest(views.html.circs.s2_report_changes.g9_employmentChange(updatedFormWithErrors))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }

  def validTypeOfWork: Constraint[String] = Constraint[String]("constraint.typeOfWork") { answer =>
    answer match {
      case `employed` => Valid
      case `selfemployed` => Valid
      case _ => Invalid(ValidationError("typeOfWork.invalid"))
    }
  }

  def validHasWorkStartedYet: Constraint[String] = Constraint[String]("constraint.hasWorkStartedYet") { answer =>
    answer match {
      case `yes` => Valid
      case `no` => Valid
      case _ => Invalid(ValidationError("hasWorkStatedYet.invalid"))
    }
  }
}
