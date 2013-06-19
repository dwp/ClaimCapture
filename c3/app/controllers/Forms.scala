package controllers

import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
import models.{MultiLineAddress, DayMonthYear}
import util.{Failure, Success, Try}
import org.joda.time.DateTime
import play.api.data.Mapping
import play.api.data.Forms._

object Forms {

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number),
    "month" -> optional(number),
    "year" -> optional(number))(DayMonthYear.apply)(DayMonthYear.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text),
    "lineTwo" -> optional(text),
    "lineThree" -> optional(text))(MultiLineAddress.apply)(MultiLineAddress.unapply)

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
    dmy => dmy match {
      case DayMonthYear(None, None, None) => Invalid(ValidationError("error.required"))
      case DayMonthYear(_, _, _) =>

        Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
          case Success(dt:DateTime) => if (dt.getYear > 9999 || dt.getYear < 999) Invalid(ValidationError("error.invalid")) else Valid
          case Failure(_) => Invalid(ValidationError("error.invalid"))
        }
    }
  }

  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty && a.lineTwo.isEmpty && a.lineThree.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }

}
