package controllers

import org.joda.time.DateTime
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import scala.util.Try
import models._
import play.api.data.validation.Constraints._
import scala.util.Success
import models.MultiLineAddress
import scala.util.Failure
import models.NationalInsuranceNumber
import scala.Some
import play.api.data.validation.ValidationError
import models.Postcode
import scala.util.matching.Regex

object Mappings {

  val maxNrOfChars = 60

  val yes = "yes"

  val no = "no"

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number),
    "month" -> optional(number),
    "year" -> optional(number),
    "hour" -> optional(number),
    "minutes" -> optional(number))(DayMonthYear.apply)(DayMonthYear.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text(maxLength = maxNrOfChars)),
    "lineTwo" -> optional(text(maxLength = maxNrOfChars)),
    "lineThree" -> optional(text(maxLength = maxNrOfChars)))(MultiLineAddress.apply)(MultiLineAddress.unapply)

  val whereabouts: Mapping[Whereabouts] = mapping(
    "location" -> nonEmptyText,
    "other" -> optional(text))(Whereabouts.apply)(Whereabouts.unapply)

  def requiredWhereabouts: Constraint[Whereabouts] = Constraint[Whereabouts]("constraint.required") {
    whereabouts =>
      whereabouts match {
        case Whereabouts(s, _) => if (s.isEmpty) Invalid(ValidationError("error.required")) else Valid
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
    dmy =>
      dmy match {
        case DayMonthYear(None, None, None, _, _) => Invalid(ValidationError("error.required"))
        case DayMonthYear(_, _, _, _, _) => dateTimeValidation(dmy)
      }
  }

  def validDateOnly: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.validateDate") { dmy =>
    dateTimeValidation(dmy)
  }

  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty && a.lineTwo.isEmpty && a.lineThree.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }

  
  
  
  def nino: Mapping[NationalInsuranceNumber] = mapping(
    "ni1" -> optional(nonEmptyText),
    "ni2" -> optional(number),
    "ni3" -> optional(number),
    "ni4" -> optional(number),
    "ni5" -> optional(nonEmptyText))(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

    
  def ninoValidation(nino: NationalInsuranceNumber): ValidationResult = {
    val ninoPattern = """[A-CEGHJ-PR-TW-Z]{2}[0-9]{6}[ABCD\S]{1}""".r
    val ninoConcatenated = nino.ni1.get + nino.ni2.get + nino.ni3.get + nino.ni4.get + nino.ni5.get
    ninoPattern.pattern.matcher(ninoConcatenated).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.nationalInsuranceNumber"))
    }
  }

  def validNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.required") {
    nino =>
      nino match {
        case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => ninoValidation(nino)
        case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
      }
  }

  def validNinoOnly: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.validNationalInsuranceNumber") { nino =>
    ninoValidation(nino)
  }

  def postcode: Mapping[Postcode] = mapping(
    "content" -> optional(text verifying (pattern("""^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$""".r,
      "constraint.postcode", "error.postcode"), maxLength(10))))(Postcode.apply)(Postcode.unapply)

  def validPostcode: Constraint[Postcode] = Constraint[Postcode]("constraint.postcode") {
    p =>
      p match {
        case Postcode(Some(_)) => Valid
        case _ => Invalid(ValidationError("error.postcode"))
      }
  }
}
