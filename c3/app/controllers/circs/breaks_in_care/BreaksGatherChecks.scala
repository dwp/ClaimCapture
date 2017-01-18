package controllers.circs.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.DayMonthYear
import models.domain.{CircumstancesYourDetails, CircsBreak, Claim, TheirPersonalDetails}
import models.yesNo.YesNoWithDate
import org.joda.time.DateTime
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.{Failure, Success, Try}

trait BreaksGatherChecks {
  def requiredWhenWereYouAdmitted: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakWhenWereYouAdmitted") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.whenWereYouAdmitted.isDefined) => Invalid(ValidationError("whenWereYouAdmitted"))
      case _ => validateDate(break.whenWereYouAdmitted.get, "whenWereYouAdmitted.invalid")
    }
  }

  def requiredYourStayEndedAnswer: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakYourStayEndedAnswer") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.yourStayEnded.isDefined) => Invalid(ValidationError("yourStayEnded.answer"))
      case _ => Valid
    }
  }

  def requiredYourStayEndedDate: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakYourStayEndedDate") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (break.yourStayEnded.isDefined && !YesNoWithDate.validate(break.yourStayEnded.get)) => Invalid(ValidationError("yourStayEnded.date"))
      case _ if (break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => validateDate(break.yourStayEnded.get.date.get, "yourStayEnded.date.invalid")
      case _ => Valid
    }
  }

  def requiredWhenWasDpAdmitted: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakWhenWasDpAdmitted") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.whenWasDpAdmitted.isDefined) => Invalid(ValidationError("whenWasDpAdmitted"))
      case _ => validateDate(break.whenWasDpAdmitted.get, "whenWasDpAdmitted.invalid")
    }
  }

  def requiredDpStayEndedAnswer: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakDpStayEndedAnswer") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.dpStayEnded.isDefined) => Invalid(ValidationError("dpStayEnded.answer"))
      case _ => Valid
    }
  }

  def requiredDpStayEndedDate: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakDpStayEndedDate") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (break.dpStayEnded.isDefined && !YesNoWithDate.validate(break.dpStayEnded.get)) => Invalid(ValidationError("dpStayEnded.date"))
      case _ if (break.dpStayEnded.isDefined && break.dpStayEnded.get.answer == Mappings.yes) => validateDate(break.dpStayEnded.get.date.get, "dpStayEnded.date.invalid")
      case _ => Valid
    }
  }

  def requiredBreaksInCareStillCaring: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breaksInCareStillCaring") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.breaksInCareStillCaring.isDefined) => Invalid(ValidationError("breaksInCareStillCaring"))
      case _ => break.breaksInCareStillCaring.get match {
        case `yes` => Valid
        case `no` => Valid
        case _ => Invalid(ValidationError("breaksInCareStillCaring.invalidYesNo"))
      }
    }
  }

  def validateDate(dmy: DayMonthYear, field: String) = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError(field))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError(field))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError(field))
  }


  def requiredStartDateNotAfterEndDate(): Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breaksInCareDateRange") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You if (break.whenWereYouAdmitted.isDefined && break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => checkDatesIncrease(break.whenWereYouAdmitted, break.yourStayEnded.get.date, "yourStayEnded.invalidDateRange")
      case BreaksInCareGatherOptions.DP if (break.whenWasDpAdmitted.isDefined && break.dpStayEnded.isDefined && break.dpStayEnded.get.answer == Mappings.yes) => checkDatesIncrease(break.whenWasDpAdmitted, break.dpStayEnded.get.date, "dpStayEnded.invalidDateRange")
      case _ => Valid
    }
  }

  def checkDatesIncrease(startDate: Option[DayMonthYear], endDate: Option[DayMonthYear], field: String) = {
    (startDate.isDefined && endDate.isDefined) match {
      case true if (startDate.get.isAfter(endDate.get)) => Invalid (ValidationError(field))
      case _ => Valid
    }
  }

  def requiredMedicalProfessional: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.medicalProfessional") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You =>
        checkValidYesNo(break.yourMedicalProfessional, "yourMedicalProfessional", "yourMedicalProfessional.invalidYesNo")
      case BreaksInCareGatherOptions.DP =>
        checkValidYesNo(break.dpMedicalProfessional, "dpMedicalProfessional", "dpMedicalProfessional.invalidYesNo")
    }
  }

  def checkValidYesNo(medicalProfessional: Option[String], field: String, fieldYesNo: String) = {
    medicalProfessional.isDefined match {
      case false => Invalid(ValidationError(field))
      case _ => medicalProfessional.get match {
        case `yes` => Valid
        case `no` => Valid
        case _ => Invalid(ValidationError(fieldYesNo))
      }
    }
  }

  def circsDpName(claim: Claim) = {
    val yourDetails=claim.questionGroup[CircumstancesYourDetails].getOrElse(CircumstancesYourDetails()).asInstanceOf[CircumstancesYourDetails]
    yourDetails.theirFirstName + " " + yourDetails.theirSurname
  }
}
