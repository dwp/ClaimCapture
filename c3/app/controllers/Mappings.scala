package controllers

import org.joda.time.DateTime
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import scala.util.Try
import models._
import domain.MoreAboutYourPartner
import scala.util.Success
import models.MultiLineAddress
import scala.util.Failure
import models.NationalInsuranceNumber
import scala.Some
import play.api.data.validation.ValidationError

object Mappings {

  val sixty = 60

  val two = 2

  val hundred = 100

  val yes = "yes"

  val no = "no"

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number),
    "month" -> optional(number),
    "year" -> optional(number),
    "hour" -> optional(number),
    "minutes" -> optional(number)
  )(DayMonthYear.apply)(DayMonthYear.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(text(maxLength = sixty)),
    "lineTwo" -> optional(text(maxLength = sixty)),
    "lineThree" -> optional(text(maxLength = sixty))
  )(MultiLineAddress.apply)(MultiLineAddress.unapply)

  val whereabouts: Mapping[Whereabouts] = mapping(
    "location" -> nonEmptyText,
    "other" -> optional(text)
  )(Whereabouts.apply)(Whereabouts.unapply)

  val sortCode: Mapping[SortCode] = mapping(
    "sort1" -> text( maxLength = two),
    "sort2" -> text( maxLength = two),
    "sort3" -> text( maxLength = two)
  )(SortCode.apply)(SortCode.unapply)

  def requiredSortCode: Constraint[SortCode] = Constraint[SortCode]("constraint.required"){ sortCode =>
    sortCode match {
      case SortCode(s1,s2,s3) => if (s1.isEmpty || s2.isEmpty || s3.isEmpty) Invalid(ValidationError("error.required"))
                                 else if (!(areAllDigits(s1) && areAllDigits(s2) && areAllDigits(s3))) Invalid(ValidationError("error.number"))
                                 else Valid
    }
  }

  def areAllDigits(x: String) = x forall Character.isDigit

  def requiredWhereabouts: Constraint[Whereabouts] = Constraint[Whereabouts]("constraint.required") { whereabouts =>
    whereabouts match {
      case Whereabouts(s, _) => if (s.isEmpty) Invalid(ValidationError("error.required")) else Valid
    }
  }

  def dateTimeValidation(dmy: DayMonthYear): ValidationResult = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") { dmy =>
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
    "ni1" -> optional(text),
    "ni2" -> optional(text),
    "ni3" -> optional(text),
    "ni4" -> optional(text),
    "ni5" -> optional(text))(NationalInsuranceNumber.apply)(NationalInsuranceNumber.unapply)

  private def ninoValidation(nino: NationalInsuranceNumber): ValidationResult = {
    val ninoPattern = """[A-CEGHJ-PR-TW-Z]{2}[0-9]{6}[ABCD]""".r
    val ninoConcatenated = nino.ni1.get + nino.ni2.get + nino.ni3.get + nino.ni4.get + nino.ni5.get

    ninoPattern.pattern.matcher(ninoConcatenated.toUpperCase).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.nationalInsuranceNumber"))
    }
  }

  def validNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.nino") {
    nino =>
      nino match {
        case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => ninoValidation(nino)
        case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
      }
  }

  def validNinoOnly: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.validNationalInsuranceNumber") { nino =>
    ninoValidation(nino)
  }

  def validPostcode:Constraint[String]= Constraint[String]("constraint.postcode") { postcode  =>
    val postcodePattern = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})$""".r
    postcodePattern.pattern.matcher(postcode).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.postcode"))
    }
  }

  def validPhoneNumber:Constraint[String] = Constraint[String]("constraint.phoneNumber") { phoneNumber =>
    val phoneNumberPattern = """[0-9 \-]{1,20}""".r
    phoneNumberPattern.pattern.matcher(phoneNumber).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.invalid"))
    }
  }

  def validDecimalNumber:Constraint[String] = Constraint[String]("constraint.decimal") { decimal =>
    val decimalPattern = """^[0-9]{1,12}(\.[0-9])?$""".r
    decimalPattern.pattern.matcher(decimal).matches match {
      case true => Valid
      case false => Invalid(ValidationError("decimal.invalid"))
    }
  }

  def validYesNo:Constraint[String] = Constraint[String]("constraint.yesNo") { answer =>
    answer match {
      case `yes` => Valid
      case `no` => Valid
      case _ =>  Invalid(ValidationError("yesNo.invalid"))
    }
  }

}