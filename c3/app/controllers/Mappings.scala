package controllers

import scala.util.Try
import scala.util.{Failure, Success}
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import play.api.data.validation.ValidationError
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import models._

object Mappings {
  object Name {
    val maxLength = 35
  }

  val fifty = 50

  val sixty = 60

  val two = 2

  val hundred = 100

  val yes = "yes"

  val no = "no"

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number(max = 100)),
    "month" -> optional(number(max = 100)),
    "year" -> optional(number(max = 99999)),
    "hour" -> optional(number(max = 100, min = 0)),
    "minutes" -> optional(number(max = 100, min = 0)))(DayMonthYear.apply)(DayMonthYear.unapply)

  val periodFromTo: Mapping[PeriodFromTo] = mapping(
    "from" -> dayMonthYear.verifying(validDate),
    "to" -> dayMonthYear.verifying(validDate))(PeriodFromTo.apply)(PeriodFromTo.unapply)

  val street: Mapping[Street] = mapping(
    "lineOne" -> optional(text(maxLength = 35).verifying(simpleTextLine).verifying(Constraints.nonEmpty))
  )(Street.apply)(Street.unapply)

  val town: Mapping[Town] = mapping(
    "lineTwo" -> optional(text(maxLength = 35).verifying(simpleTextLine)),
    "lineThree" -> optional(text(maxLength = 35).verifying(simpleTextLine))
  )(Town.apply)(Town.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "street" -> (street verifying requiredStreet),
    "town" -> optional(town)
    )(MultiLineAddress.apply)(MultiLineAddress.unapply)

  val whereabouts: Mapping[Whereabouts] = mapping(
    "location" -> nonEmptyText(maxLength = 35),
    "location.other" -> optional(text(maxLength = 35)))(Whereabouts.apply)(Whereabouts.unapply)

  val paymentFrequency: Mapping[PaymentFrequency] = mapping(
    "frequency" -> text(maxLength = sixty),
    "frequency.other" -> optional(text(maxLength = sixty)))(PaymentFrequency.apply)(PaymentFrequency.unapply)

  val pensionPaymentFrequency: Mapping[PensionPaymentFrequency] = mapping(
    "frequency" -> nonEmptyText(maxLength = sixty),
    "frequency.other" -> optional(text(maxLength = sixty)))(PensionPaymentFrequency.apply)(PensionPaymentFrequency.unapply)

  val sortCode: Mapping[SortCode] = mapping(
    "sort1" -> text(maxLength = two),
    "sort2" -> text(maxLength = two),
    "sort3" -> text(maxLength = two))(SortCode.apply)(SortCode.unapply)

  def required[T](mapping: Mapping[T]): Mapping[T] = {
    def required: Constraint[T] = Constraint[T]("constraint.required") { t => Valid }

    mapping.verifying(required)
  }

  def dayMonthYear(datePatterns: String*): Mapping[DayMonthYear] = mapping(
    "date" -> nonEmptyText.verifying(validDayMonthYear(datePatterns: _*)).transform(stringToDayMonthYear(datePatterns: _*), (dmy: DayMonthYear) => dmy.`dd/MM/yyyy`),
    "hour" -> optional(number(min = 0, max = 24)),
    "minutes" -> optional(number(min = 0, max = 60))
  )((dmy, h, m) => (h, m) match {
      case (Some(h), Some(m)) => dmy.withTime(h, m)
      case _ => dmy
    }
   )((dmy: DayMonthYear) => Some((dmy, dmy.hour, dmy.minutes)))

  def validDayMonthYear(datePatterns: String*) = (date: String) => Try(stringToDayMonthYear(datePatterns: _*)(date)).isSuccess

  def stringToDayMonthYear(datePatterns: String*) = (date: String) => {
    val datePatternDefault = "dd/MM/yyyy"

    def jodaDateTime(datePattern: String) = DateTimeFormat.forPattern(datePattern).parseDateTime(date)

    def dmy(datePattern: String) = {
      val jdt = jodaDateTime(datePattern)
      DayMonthYear(jdt.getDayOfMonth, jdt.getMonthOfYear, jdt.getYear)
    }

    def dayMonthYear(dps: List[String]): DayMonthYear = dps match {
      case Nil => dmy(datePatternDefault)
      case h :: t => Try(dmy(h)).getOrElse(dayMonthYear(t))
    }

    dayMonthYear(if (datePatterns.isEmpty) List(datePatternDefault, "dd/MM/yyyy") else datePatterns.toList)
  }

  def requiredStreet: Constraint[Street] = Constraint[Street]("constraint.required") { street =>
    street match {
      case Street(s) if s.isDefined => Valid
      case _ => Invalid(ValidationError("error.required"))
    }
  }

  def requiredSortCode: Constraint[SortCode] = Constraint[SortCode]("constraint.required") { sortCode =>
    sortCode match {
      case SortCode(s1, s2, s3) => if (s1.isEmpty || s2.isEmpty || s3.isEmpty) Invalid(ValidationError("error.required"))
      else if (!(areAllDigits(s1) && areAllDigits(s2) && areAllDigits(s3))) Invalid(ValidationError("error.number"))
      else Valid
    }
  }

  def areAllDigits(x: String) = x forall Character.isDigit

  def requiredWhereabouts: Constraint[Whereabouts] = Constraint[Whereabouts]("constraint.required") { whereabouts =>
    whereabouts match {
      case Whereabouts(location, other) =>
        if (location.isEmpty) Invalid(ValidationError("error.required"))
        else if (location == app.Whereabouts.Other && other.isEmpty) Invalid(ValidationError("error.required"))
        else Valid
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

  def filledInNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.required") { nino =>
    nino match {
      case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => Valid
      case _ => Invalid(ValidationError("error.required"))
    }
  }

  def validNino: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.nino") { nino =>
    nino match {
      case NationalInsuranceNumber(Some(_), Some(_), Some(_), Some(_), Some(_)) => ninoValidation(nino)
      case _ => Invalid(ValidationError("error.nationalInsuranceNumber"))
    }
  }

  def validNinoOnly: Constraint[NationalInsuranceNumber] = Constraint[NationalInsuranceNumber]("constraint.validNationalInsuranceNumber") {
    nino =>
      ninoValidation(nino)
  }

  def validPostcode: Constraint[String] = Constraint[String]("constraint.postcode") { postcode =>
    val postcodePattern = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})$""".r

    postcodePattern.pattern.matcher(postcode).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.postcode"))
    }
  }

  def validPhoneNumber: Constraint[String] = Constraint[String]("constraint.phoneNumber") { phoneNumber =>
    val phoneNumberPattern = """[0-9 \-]{1,20}""".r

    phoneNumberPattern.pattern.matcher(phoneNumber).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.invalid"))
    }
  }

  def validDecimalNumber: Constraint[String] = Constraint[String]("constraint.decimal") { decimal =>
    val decimalPattern = """^[0-9]{1,12}(\.[0-9]{1,2})?$""".r

    decimalPattern.pattern.matcher(decimal).matches match {
      case true => Valid
      case false => Invalid(ValidationError("decimal.invalid"))
    }
  }

  def validNumber: Constraint[String] = Constraint[String]("constraint.number") { number =>
    val numberPattern = """^[0-9]*$""".r

    numberPattern.pattern.matcher(number).
      matches match {
      case true => Valid
      case false => Invalid(ValidationError("number.invalid"))
    }
  }

  def validYesNo: Constraint[String] = Constraint[String]("constraint.yesNo") { answer =>
    answer match {
      case `yes` => Valid
      case `no` => Valid
      case _ => Invalid(ValidationError("yesNo.invalid"))
    }
  }
  
  def paymentFrequencyValidation(pf: PaymentFrequency): ValidationResult = Try(new PaymentFrequency(pf.frequency, pf.other)) match {
    case Success(p: PaymentFrequency) if p.frequency.toLowerCase == app.PensionPaymentFrequency.Other && p.other.isEmpty => Invalid(ValidationError("error.paymentFrequency"))
    case Success(p: PaymentFrequency) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validPaymentFrequencyOnly: Constraint[PaymentFrequency] = Constraint[PaymentFrequency]("constraint.validatePaymentFrequency") {
    pf => paymentFrequencyValidation(pf)
  }

  def pensionPaymentFrequencyValidation(pf: PensionPaymentFrequency): ValidationResult = Try(new PensionPaymentFrequency(pf.frequency, pf.other)) match {
    case Success(p: PensionPaymentFrequency) if p.frequency.toLowerCase == "other" && p.other.isEmpty => Invalid(ValidationError("error.paymentFrequency"))
    case Success(p: PensionPaymentFrequency) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validPensionPaymentFrequencyOnly: Constraint[PensionPaymentFrequency] = Constraint[PensionPaymentFrequency]("constraint.validatePaymentFrequency") {
    pf => pensionPaymentFrequencyValidation(pf)
  }

  def validNationality: Constraint[String] = Constraint[String]("constraint.nationality") { nationality =>
    val nationalityPattern = """[a-zA-Z \-]{1,60}""".r

    nationalityPattern.pattern.matcher(nationality).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.nationality"))
    }
  }

  def validForbiddenCharacters: Constraint[String] = Constraint[String]("constraint.forbiddenCharacters") { input =>
    val forbiddenPattern = """.*[<>;].*""".r

    forbiddenPattern.pattern.matcher(input).matches match {
      case false => Valid
      case true => Invalid(ValidationError("error.forbidden.characters"))

    }
  }

  def simpleTextLine: Constraint[String] = Constraint[String]("constraint.simpleTextLine") { simpleTextLine =>
    val simpleTextLinePattern = """^[a-zA-Z0-9 ]*$""".r

    simpleTextLinePattern.pattern.matcher(simpleTextLine).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.address.characters"))
    }
  }

  def restrictedStringText: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    // This is the same allowable characters as per the xml schema with some characters removed
    // The removed characters are : £()@<>
    val restrictedStringPattern = """^[A-Za-z0-9\s~!"#$%&'\*\+,\-\./:;=\?\[\\\]_\{\}\^]*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.characters"))
    }
  }

}