package controllers.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.DayMonthYear
import models.domain.{TheirPersonalDetails, Claim, Break}
import models.yesNo.YesNoWithDate
import org.joda.time.DateTime
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

import scala.util.{Failure, Success, Try}

/**
  * Created by peterwhitehead on 08/08/2016.
  */
trait BreaksGatherChecks {
  def requiredWhenWereYouAdmitted: Constraint[Break] = Constraint[Break]("constraint.breakWhenWereYouAdmitted") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.whenWereYouAdmitted.isDefined) => Invalid(ValidationError("whenWereYouAdmitted"))
      case _ => validateDate(break.whenWereYouAdmitted.get, "whenWereYouAdmitted.invalid")
    }
  }

  def requiredYourStayEndedAnswer: Constraint[Break] = Constraint[Break]("constraint.breakYourStayEndedAnswer") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.yourStayEnded.isDefined) => Invalid(ValidationError("yourStayEnded.answer"))
      case _ => Valid
    }
  }

  def requiredYourStayEndedDate: Constraint[Break] = Constraint[Break]("constraint.breakYourStayEndedDate") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (break.yourStayEnded.isDefined && !YesNoWithDate.validate(break.yourStayEnded.get)) => Invalid(ValidationError("yourStayEnded.date"))
      case _ if (break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => validateDate(break.yourStayEnded.get.date.get, "yourStayEnded.date.invalid")
      case _ => Valid
    }
  }

  def requiredWhenWasDpAdmitted: Constraint[Break] = Constraint[Break]("constraint.breakWhenWasDpAdmitted") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.whenWasDpAdmitted.isDefined) => Invalid(ValidationError("whenWasDpAdmitted"))
      case _ => validateDate(break.whenWasDpAdmitted.get, "whenWasDpAdmitted.invalid")
    }
  }

  def requiredDpStayEndedAnswer: Constraint[Break] = Constraint[Break]("constraint.breakDpStayEndedAnswer") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.dpStayEnded.isDefined) => Invalid(ValidationError("dpStayEnded.answer"))
      case _ => Valid
    }
  }

  def requiredDpStayEndedDate: Constraint[Break] = Constraint[Break]("constraint.breakDpStayEndedDate") { break =>
    break.whoWasAway match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (break.dpStayEnded.isDefined && !YesNoWithDate.validate(break.dpStayEnded.get)) => Invalid(ValidationError("dpStayEnded.date"))
      case _ if (break.dpStayEnded.isDefined && break.dpStayEnded.get.answer == Mappings.yes) => validateDate(break.dpStayEnded.get.date.get, "dpStayEnded.date.invalid")
      case _ => Valid
    }
  }

  def requiredBreaksInCareStillCaring: Constraint[Break] = Constraint[Break]("constraint.breaksInCareStillCaring") { break =>
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


  def requiredStartDateNotAfterEndDate(): Constraint[Break] = Constraint[Break]("constraint.breaksInCareDateRange") { break =>
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

  def requiredMedicalProfessional: Constraint[Break] = Constraint[Break]("constraint.medicalProfessional") { break =>
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

  def dpDetails(claim: Claim) : String = {
    val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }
}
