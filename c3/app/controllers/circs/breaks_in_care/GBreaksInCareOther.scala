package controllers.circs.breaks_in_care

import app.BreaksInCareOtherOptions
import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import gov.dwp.carers.xml.validation.CommonValidation._
import models.domain._
import models.view.{CachedChangeOfCircs, CachedClaim}
import models.yesNo.{RadioWithText, YesNoWithDate}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation._
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.Controller
import utils.helpers.CarersForm._

object GBreaksInCareOther extends Controller with CachedChangeOfCircs with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val whereWasDpMapping = "whereWasDp" -> optional(radioWithText)

  val whereWereYouMapping = "whereWereYou" -> optional(radioWithText)

  val caringStartedMapping = "caringStarted" -> optional(yesNoWithDate)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.another),
    "whoWasInHospital" -> default(carersNonEmptyText, ""),
    "whenWereYouAdmitted" -> default(optional(dayMonthYear), None),
    "yourStayEnded" -> default(optional(yesNoWithDate), None),
    "whenWasDpAdmitted" -> default(optional(dayMonthYear), None),
    "dpStayEnded" -> default(optional(yesNoWithDate), None),
    "breaksInCareStillCaring" -> default(optional(nonEmptyText), None),
    "yourMedicalProfessional" -> default(optional(nonEmptyText), None),
    "dpMedicalProfessional" -> default(optional(nonEmptyText), None),
    "caringEnded.date" -> optional(dayMonthYear),
    caringStartedMapping,
    whereWasDpMapping,
    whereWereYouMapping,
    "caringEnded.time" -> optional(text),
    "caringStarted.time" -> optional(text)
  )(CircsBreak.apply)(CircsBreak.unapply)
    .verifying(requiredOtherCaringEnded)
    .verifying(validateOptionalCarersNonEmptyTextEnded)
    .verifying(requiredOtherStartDateNotAfterEndDate)
    .verifying(requiredCaringStartedAnswer)
    .verifying(requiredCaringStartedDate)
    .verifying(validateOptionalCarersNonEmptyTextStarted)
    .verifying(requiredWhereWasDpRadioWithText)
    .verifying(requiredwhereWereYouRadioWithText)
  )

  val backCall = routes.GBreaksInCareSummary.present()

  def present(iterationID: String) = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    val break = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(CircsBreak())
    Ok(views.html.circs.breaks_in_care.breaksInCareOther(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val dp = dpDetails(circs);
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "caringEnded", FormError("caringEnded.date", errorRequiredWithError, Seq(dp)))
          .replaceError("", "caringEnded.invalid", FormError("caringEnded.date", errorInvalidWithError, Seq(dp)))
          .replaceError("", "caringEnded.invalidDateRange", FormError("caringStarted.date", errorInvalidRestartDateRange))
          .replaceError("", "caringEnded.time", FormError("caringEnded.time", errorRestrictedCharacters))
          .replaceError("", "caringStarted.answer", FormError("caringStarted.answer", errorRequired))
          .replaceError("", "caringStarted.date", FormError("caringStarted.date", errorRequired))
          .replaceError("", "caringStarted.date.invalid", FormError("caringStarted.date", errorInvalid))
          .replaceError("", "caringStarted.time", FormError("caringStarted.time", errorRestrictedCharacters))
          .replaceError("", "whereWasDp", FormError("whereWasDp", errorRequired, Seq(dp)))
          .replaceError("", "whereWasDp.text", FormError("whereWasDp.text", errorRequired))
          .replaceError("", "whereWasDp.text.restricted", FormError("whereWasDp.text", errorRestrictedCharacters))
          .replaceError("", "whereWereYou", FormError("whereWereYou", errorRequired))
          .replaceError("", "whereWereYou.text", FormError("whereWereYou.text", errorRequired))
          .replaceError("", "whereWereYou.text.restricted", FormError("whereWereYou.text", errorRestrictedCharacters))
        BadRequest(views.html.circs.breaks_in_care.breaksInCareOther(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare = breaksInCare.update(break).breaks.size match {
          case noOfBreaks if (noOfBreaks > CircsBreaks.maximumBreaks) => breaksInCare
          case _ => breaksInCare.update(break)
        }
        val updatedClaim = updateClaim(updatedBreaksInCare)
        updatedClaim -> Redirect(routes.GBreaksInCareSummary.present())
      })
  }

  private def updateClaim(newbreaks: CircsBreaksInCare)(implicit circs: Claim) = {
    val updatedBreaks = updatedBreakTypesObject
    val updatedClaim = circs.update(updatedBreaks).update(newbreaks)
    updatedClaim
  }

  private def updatedBreakTypesObject(implicit circs: Claim) = {
    // Delete the other answer from claim. Otherwise, it will prepopulate the answer when return to Summary page
    // But if we are the last break type being collected, clear all answers so that summary page needs to be re-selected.
    def breaksTypes(implicit claim: Claim) = claim.questionGroup[CircsBreaksInCareType].getOrElse(CircsBreaksInCareType())
    if (breaksTypes.hospital.isDefined || breaksTypes.carehome.isDefined) {
      breaksTypes.copy(other = None)
    }
    else {
      new CircsBreaksInCareType()
    }
  }

  private def requiredOtherCaringEnded: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakCaringEnded") { break =>
    break.caringEnded.isDefined match {
      case false => Invalid(ValidationError("caringEnded"))
      case _ => validateDate(break.caringEnded.get, "caringEnded.invalid")
    }
  }

  private def requiredOtherStartDateNotAfterEndDate(): Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breaksInCareDateRange") { break =>
    break.caringEnded.isDefined match {
      case true if (break.caringStarted.isDefined && break.caringStarted.get.answer == Mappings.yes) => checkDatesIncrease(break.caringEnded, break.caringStarted.get.date, "caringEnded.invalidDateRange")
      case _ => Valid
    }
  }

  private def requiredCaringStartedAnswer: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakCaringStartedAnswer") { break =>
    break.caringStarted.isDefined match {
      case false => Invalid(ValidationError("caringStarted.answer"))
      case _ => Valid
    }
  }

  private def requiredCaringStartedDate: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakCaringStartedDate") { break =>
    break.caringStarted.isDefined match {
      case true if !YesNoWithDate.validate(break.caringStarted.get) => Invalid(ValidationError("caringStarted.date"))
      case true if (break.caringStarted.get.answer == Mappings.yes) => validateDate(break.caringStarted.get.date.get, "caringStarted.date.invalid")
      case _ => Valid
    }
  }

  private def requiredwhereWereYouRadioWithText: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.radioWithText") { break =>
    break.whereWereYou.isDefined match {
      case true if (!RadioWithText.validateOnOther(break.whereWereYou.get)) => Invalid(ValidationError("whereWereYou.text"))
      case true if (break.whereWereYou.get == BreaksInCareOtherOptions.SomewhereElse) => restrictedStringCheck(break.whereWereYou.get.text.get, "whereWereYou.text.restricted")
      case false if (break.caringStarted.isDefined && break.caringStarted.get.answer == Mappings.yes) => Invalid(ValidationError("whereWereYou"))
      case _ => Valid
    }
  }

  private def requiredWhereWasDpRadioWithText: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.radioWithText") { break =>
    break.whereWasDp.isDefined match {
      case true if (!RadioWithText.validateOnOther(break.whereWasDp.get)) => Invalid(ValidationError("whereWasDp.text"))
      case true if (break.whereWasDp.get == BreaksInCareOtherOptions.SomewhereElse) => restrictedStringCheck(break.whereWasDp.get.text.get, "whereWasDp.text.restricted")
      case false if (break.caringStarted.isDefined && break.caringStarted.get.answer == Mappings.yes) => Invalid(ValidationError("whereWasDp"))
      case _ => Valid
    }
  }

  private def validateOptionalCarersNonEmptyTextStarted: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.time") { break =>
    (break.caringStarted.isDefined, break.caringStartedTime.isDefined) match {
      case (true, true) if (!break.caringStartedTime.getOrElse("").isEmpty) => restrictedStringCheck(break.caringStartedTime.get, "caringStarted.time")
      case _ => Valid
    }
  }

  private def restrictedStringCheck(restrictedString: String, error: String) = {
    val restrictedStringPattern = RESTRICTED_CHARS.r
    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError(error))
    }
  }

  private def validateOptionalCarersNonEmptyTextEnded: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.time") { break =>
    break.caringEndedTime.isDefined match {
      case true if (!break.caringEndedTime.getOrElse("").isEmpty) => restrictedStringCheck(break.caringEndedTime.get, "caringEnded.time")
      case _ => Valid
    }
  }

  def breaksInCare(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())
  def breaksTypes(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCareType].getOrElse(CircsBreaksInCareType())
}
