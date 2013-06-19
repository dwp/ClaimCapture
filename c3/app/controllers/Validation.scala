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

object Validation {
  def nationalInsuranceNumber: Mapping[NationalInsuranceNumber] = mapping(
    "ni1" -> optional(text),
    "ni2" -> optional(number),
    "ni3" -> optional(number),
    "ni4" -> optional(number),
    "ni5" -> optional(text))(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

    
  def validNationalInsuranceNumber: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.required") {
    dmy => dmy match {
      case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => Valid // TODO: [SKW] validate AB123456C
      case _ => Invalid(ValidationError("error.invalid"))
    }
  }
}