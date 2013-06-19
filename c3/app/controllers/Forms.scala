package controllers

import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.{Invalid, Valid, Constraint}
import scala.util.Try
import org.joda.time.DateTime
import util.Failure
import play.api.data.validation.ValidationError
import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import util.Success
import play.api.data.validation.Constraints._

object Forms {
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