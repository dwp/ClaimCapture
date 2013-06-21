package controllers

import org.joda.time.DateTime
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import scala.util.Try
import util.Failure
import util.Success
import models._
import play.api.data.validation.Constraints._
import scala.util.Success
import scala.util.Failure
import scala.Some
import play.api.data.validation.ValidationError
import scala.util.Success
import models.MultiLineAddress
import scala.util.Failure
import models.NationalInsuranceNumber
import scala.Some
import play.api.data.validation.ValidationError

object Mappings {

  val maxNrOfChars = 60

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number),
    "month" -> optional(number),
    "year" -> optional(number),
    "hour" -> optional(number),
    "minutes" -> optional(number))(DayMonthYear.apply)(DayMonthYear.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text),
    "lineTwo" -> optional(text),
    "lineThree" -> optional(text))(MultiLineAddress.apply)(MultiLineAddress.unapply)

  val whereabouts: Mapping[Whereabouts] = mapping(
    "location" -> nonEmptyText,
    "other" -> optional(text)
  )(Whereabouts.apply)(Whereabouts.unapply)

  def requiredWhereabouts: Constraint[Whereabouts] = Constraint[Whereabouts]("constraint.required"){
    whereabouts => whereabouts match {
      case Whereabouts(s,_) => if (s.isEmpty()) Invalid(ValidationError("error.required")) else Valid
    }
  }
  def dateTimeValidation(dmy: DayMonthYear): ValidationResult = {
    Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
      case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
      case Success(dt: DateTime) => Valid
      case Failure(_) => Invalid(ValidationError("error.invalid"))
    }
  }
  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
    dmy => dmy match {
      case DayMonthYear(None ,None ,None ,_ ,_) => Invalid(ValidationError("error.required"))
      case DayMonthYear(_ ,_ ,_ ,_ ,_) => dateTimeValidation(dmy)
    }
  }

  def validDateOnly: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.validateDate") {
    dmy => dateTimeValidation(dmy)
  }

  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty && a.lineTwo.isEmpty && a.lineThree.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }

  def nationalInsuranceNumber: Mapping[NationalInsuranceNumber] = mapping(
    "ni1" -> optional(nonEmptyText verifying (minLength(2), maxLength(2), pattern ("""[A-CEGHJ-PR-TW-Z]{2}""".r, name = "constraint.pattern", error = "error.nationalInsuranceNumber"))),
    "ni2" -> optional(number(0, 99)),
    "ni3" -> optional(number(0, 99)),
    "ni4" -> optional(number(0, 99)),
    "ni5" -> optional(nonEmptyText verifying (maxLength(1), pattern ("""[ABCD\S]{1}""".r, name = "constraint.pattern", error = "error.nationalInsuranceNumber"))))(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

    
  def validNationalInsuranceNumber: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.ni") {
    dmy => dmy match {
      case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => Valid
      case _ => Invalid(ValidationError("error.invalid"))
    }
  }
}
