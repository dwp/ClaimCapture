package controllers

import org.joda.time.DateTime
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.{Invalid, Valid, Constraint}
import scala.util.Try
import util.Failure
import play.api.data.validation.ValidationError
import util.Success
import models.domain.{MultiLineAddress, DayMonthYear}
import models.NationalInsuranceNumber
import play.api.data.validation.Constraints._

object Forms {

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

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
    dmy => dmy match {
      case DayMonthYear(None, None, None,_,_) => Invalid(ValidationError("error.required"))
      case DayMonthYear(_, _, _,_,_) =>

        Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
          case Success(dt:DateTime) => if (dt.getYear > 9999 || dt.getYear < 999) Invalid(ValidationError("error.invalid")) else Valid
          case Failure(_) => Invalid(ValidationError("error.invalid"))
        }
    }
  }

  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty && a.lineTwo.isEmpty && a.lineThree.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }

  def nationalInsuranceNumber: Mapping[NationalInsuranceNumber] = mapping(
    "ni1" -> optional(nonEmptyText verifying (minLength(2), maxLength(2), pattern ("""[A-CEGHJ-PR-TW-Z]{2}""".r, name = "constraint.pattern", error = "error.pattern"))),
    "ni2" -> optional(number(0, 99)),
    "ni3" -> optional(number(0, 99)),
    "ni4" -> optional(number(0, 99)),
    "ni5" -> optional(nonEmptyText verifying (maxLength(1), pattern ("""[ABCD\s]{1}""".r, name = "constraint.pattern", error = "error.pattern"))))(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

    
  def validNationalInsuranceNumber: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.required") {
    dmy => dmy match {
      case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => Valid
      case _ => Invalid(ValidationError("error.invalid"))
    }
  }
}
