package controllers.breaks_in_care

import app.ConfigProperties._
import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import models.yesNo.{RadioWithText, YesNoWithDate}
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation._
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.Controller
import utils.CommonValidation._
import utils.helpers.CarersForm._

/**
  * Created by peterwhitehead on 03/08/2016.
  */
object GBreaksInCareOther extends Controller with CachedClaim with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val whereWasDpMapping = "whereWasDp" -> optional(radioWithText)

  val whereWereYouMapping = "whereWereYou" -> optional(radioWithText)

  val yourStayEndedMapping = "startedCaring" -> optional(yesNoWithDate)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.another),
    "whoWasInHospital" -> default(carersNonEmptyText, ""),
    "dpOtherEnded.date" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> default(optional(dayMonthYear), None),
    "dpStayEnded" -> default(optional(yesNoWithDate), None),
    "breaksInCareStillCaring" -> default(optional(nonEmptyText), None),
    "yourMedicalProfessional" -> default(optional(nonEmptyText), None),
    "dpMedicalProfessional" -> default(optional(nonEmptyText), None),
    whereWasDpMapping,
    whereWereYouMapping,
    "dpOtherEnded.time" -> optional(text),
    "startedCaring.time" -> optional(text)
  )(Break.apply)(Break.unapply)
    .verifying(requiredOtherWhenWereYouAdmitted)
    .verifying(validateOptionalCarersNonEmptyTextEnded)
    .verifying(requiredOtherStartDateNotAfterEndDate)
    .verifying(requiredOtherStayEndedAnswer)
    .verifying(requiredOtherStayEndedDate)
    .verifying(validateOptionalCarersNonEmptyTextStarted)
    .verifying(requiredWhereWhereYouRadioWithText)
    .verifying(requiredWhereWasDpRadioWithText)
  )

  val backCall = routes.GBreaksInCareType.present()

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())
    Ok(views.html.breaks_in_care.breaksInCareOther(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val dp = dpDetails(claim);
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "whenWereYouAdmitted", FormError("dpOtherEnded.date", errorRequiredWithError, Seq(dp)))
          .replaceError("", "whenWereYouAdmitted.invalid", FormError("dpOtherEnded.date", errorInvalidWithError, Seq(dp)))
          .replaceError("", "whenWereYouAdmitted.invalidDateRange", FormError("dpOtherEnded.date", errorInvalidDateRange))
          .replaceError("", "whenWereYouAdmitted.time", FormError("dpOtherEnded.time", errorRestrictedCharacters))
          .replaceError("", "yourStayEnded.answer", FormError("startedCaring.answer", errorRequired))
          .replaceError("", "yourStayEnded.date", FormError("startedCaring.date", errorRequired))
          .replaceError("", "yourStayEnded.date.invalid", FormError("startedCaring.date", errorInvalid))
          .replaceError("", "yourStayEnded.time", FormError("startedCaring.time", errorRestrictedCharacters))
          .replaceError("", "whereWasDp", FormError("whereWasDp", errorRequired, Seq(dp)))
          .replaceError("", "whereWasDp.text", FormError("whereWasDp.text", errorRequired))
          .replaceError("", "whereWereYou", FormError("whereWereYou", errorRequired))
          .replaceError("", "whereWereYou.text", FormError("whereWereYou.text", errorRequired))
        BadRequest(views.html.breaks_in_care.breaksInCareOther(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare =
          breaksInCare.update(break).breaks.size match {
            case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
            case _ => breaksInCare.update(break)
          }
        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had an  y more breaks in care since...'
        val updatedClaim = claim.update(updatedBreaksInCare).delete(BreaksInCareSummary)
        updatedClaim -> Redirect(routes.GBreaksInCareSummary.present())
      })
  }

  private def requiredOtherWhenWereYouAdmitted: Constraint[Break] = Constraint[Break]("constraint.breakWhenWereYouAdmitted") { break =>
    break.whenWereYouAdmitted.isDefined match {
      case false => Invalid(ValidationError("whenWereYouAdmitted"))
      case _ => validateDate(break.whenWereYouAdmitted.get, "whenWereYouAdmitted.invalid")
    }
  }

  private def requiredOtherStartDateNotAfterEndDate(): Constraint[Break] = Constraint[Break]("constraint.breaksInCareDateRange") { break =>
    break.whenWereYouAdmitted.isDefined match {
      case true if (break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => checkDatesAfter(break.whenWereYouAdmitted, break.yourStayEnded.get.date, "whenWereYouAdmitted.invalidDateRange")
      case _ => Valid
    }
  }

  private def requiredOtherStayEndedAnswer: Constraint[Break] = Constraint[Break]("constraint.breakOtherStayEndedAnswer") { break =>
    break.yourStayEnded.isDefined match {
      case false => Invalid(ValidationError("yourStayEnded.answer"))
      case _ => Valid
    }
  }

  private def requiredOtherStayEndedDate: Constraint[Break] = Constraint[Break]("constraint.breakOtherStayEndedDate") { break =>
    break.yourStayEnded.isDefined match {
      case true if !YesNoWithDate.validate(break.yourStayEnded.get) => Invalid(ValidationError("yourStayEnded.date"))
      case true if (break.yourStayEnded.get.answer == Mappings.yes) => validateDate(break.yourStayEnded.get.date.get, "yourStayEnded.date.invalid")
      case _ => Valid
    }
  }

  private def requiredWhereWhereYouRadioWithText: Constraint[Break] = Constraint[Break]("constraint.radioWithText") { break =>
    break.whereWhereYou.isDefined match {
      case true if (!RadioWithText.validateOnOther(break.whereWhereYou.get)) => Invalid(ValidationError("whereWereYou.text"))
      case _ if(break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => Invalid(ValidationError("whereWereYou"))
      case _ => Valid
    }
  }

  private def requiredWhereWasDpRadioWithText: Constraint[Break] = Constraint[Break]("constraint.radioWithText") { break =>
    break.whereWasDp.isDefined match {
      case true if (!RadioWithText.validateOnOther(break.whereWasDp.get)) => Invalid(ValidationError("whereWasDp.text"))
      case _ if(break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => Invalid(ValidationError("whereWasDp"))
      case _ => Valid
    }
  }

  private def validateOptionalCarersNonEmptyTextStarted: Constraint[Break] = Constraint[Break]("constraint.time") { break =>
    (break.yourStayEnded.isDefined, break.dpStayEndedTime.isDefined) match {
      case (true, true) if (!break.dpStayEndedTime.getOrElse("").isEmpty) => restrictedStringCheck(break.dpStayEndedTime.get, "yourStayEnded.time")
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

  private def validateOptionalCarersNonEmptyTextEnded: Constraint[Break] = Constraint[Break]("constraint.time") { break =>
    break.whenWereYouAdmittedTime.isDefined match {
      case true if (!break.whenWereYouAdmittedTime.getOrElse("").isEmpty) => restrictedStringCheck(break.whenWereYouAdmittedTime.get, "whenWereYouAdmitted.time")
      case _ => Valid
    }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
