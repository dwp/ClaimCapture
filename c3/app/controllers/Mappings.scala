package controllers

import scala.util.Try
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import models._
import controllers.CarersForms._
import models.SortCode
import models.PaymentFrequency
import scala.util.Failure
import models.NationalInsuranceNumber
import scala.Some
import play.api.data.validation.ValidationError
import models.Whereabouts
import models.PensionPaymentFrequency
import scala.util.Success
import models.MultiLineAddress
import models.PeriodFromTo
import play.api.i18n.Lang

import scala.util.matching.Regex

object Mappings {
  object Name {
    val maxLength = 35
  }

  val fifty = 50

  val sixty = 60

  val thirtyfive = 35

  val two = 2

  val four = 4

  val seventeen = 17

  val hundred = 100

  val threeHundred = 300

  val twoThousand = 2000

  val yes = "yes"

  val no = "no"

  val dontknow = "dontknow"

  val errorRequired = "error.required"
  val required = "required"

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(text),
    "month" -> optional(text),
    "year" -> optional(text),
    "hour" -> optional(text),
    "minutes" -> optional(text))(DayMonthYear.convert)(DayMonthYear.extract)



  val periodFromTo: Mapping[PeriodFromTo] = mapping(
    "from" -> dayMonthYear.verifying(validDate),
    "to" -> dayMonthYear.verifying(validDate))(PeriodFromTo.apply)(PeriodFromTo.unapply)

  val address: Mapping[MultiLineAddress] = mapping(
    "lineOne" -> optional(carersText(maxLength = 35)),
    "lineTwo" -> optional(carersText(maxLength = 35)),
    "lineThree" -> optional(carersText(maxLength = 35)))(MultiLineAddress.apply)(MultiLineAddress.unapply)

  val whereabouts: Mapping[Whereabouts] = mapping(
    "location" -> carersNonEmptyText(maxLength = 35),
    "location.other" -> optional(carersText(maxLength = sixty)))(Whereabouts.apply)(Whereabouts.unapply)

  val paymentFrequency: Mapping[PaymentFrequency] = mapping(
    "frequency" -> text(maxLength = sixty),
    "frequency.other" -> optional(carersText(maxLength = sixty)))(PaymentFrequency.apply)(PaymentFrequency.unapply)

  val mandatoryPaymentFrequency: Mapping[PaymentFrequency] = mapping(
    "frequency" -> nonEmptyText(maxLength = sixty),
    "frequency.other" -> optional(carersText(maxLength = sixty)))(PaymentFrequency.apply)(PaymentFrequency.unapply)

  val pensionPaymentFrequency: Mapping[PensionPaymentFrequency] = mapping(
    "frequency" -> carersNonEmptyText(maxLength = sixty),
    "frequency.other" -> optional(carersNonEmptyText(maxLength = sixty)))(PensionPaymentFrequency.apply)(PensionPaymentFrequency.unapply)

  val sortCode: Mapping[SortCode] = mapping(
    "sort1" -> carersText(maxLength = two),
    "sort2" -> carersText(maxLength = two),
    "sort3" -> carersText(maxLength = two))(SortCode.apply)(SortCode.unapply)

  val reasonForBeingThere: Mapping[ReasonForBeingThere] = mapping(
    "reason" -> optional(carersText(maxLength = 35)),
    "reason.other" -> optional(carersText(maxLength = sixty)))(ReasonForBeingThere.apply)(ReasonForBeingThere.unapply)

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

  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }

  def requiredAddressWithTwoLines: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if(a.lineOne.isEmpty) Invalid(ValidationError("error.required"))
    else if ((!a.lineOne.isEmpty) && (a.lineTwo.isEmpty)) Invalid(ValidationError("error.addressLines.required"))
    else Valid
  }

  def requiredSortCode: Constraint[SortCode] = Constraint[SortCode]("constraint.required") {
    case SortCode(s1, s2, s3) =>
      if (s1.length < 2 || s2.length < 2 || s3.length < 2) Invalid(ValidationError("error.sortcode.length"))
      else if (!(areAllDigits(s1) && areAllDigits(s2) && areAllDigits(s3))) Invalid(ValidationError("error.number"))
      else Valid
  }

  def areAllDigits(x: String) = x forall Character.isDigit

  def requiredWhereabouts: Constraint[Whereabouts] = Constraint[Whereabouts]("constraint.required") {
    case Whereabouts(location, other) =>
      if (location.isEmpty) Invalid(ValidationError("error.required"))
      else if (location == app.Whereabouts.Other && other.isEmpty) Invalid(ValidationError("error.required"))
      else Valid

  }

  def requiredReasonForBeingThereOther: Constraint[ReasonForBeingThere] = Constraint[ReasonForBeingThere]("constraint.required") {
    case ReasonForBeingThere(why, other) =>
      why match {
        case Some(reason) =>
          if (reason == app.ReasonForBeingThere.Other.toLowerCase() && !other.isDefined) Invalid(ValidationError("error.required")) else Valid
        case _ => Valid
      }
  }

  def requiredFrequency: Constraint[PaymentFrequency] = Constraint[PaymentFrequency]("constraint.required") {
    case PaymentFrequency(frequency,other) =>
      if (frequency.isEmpty) Invalid(ValidationError("error.required"))
      else if (frequency == app.Whereabouts.Other && other.isEmpty) Invalid(ValidationError("error.required"))
      else Valid
  }

  private def dateValidation(dmy: DayMonthYear): ValidationResult = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
      case DayMonthYear(None, None, None, _, _) => Invalid(ValidationError("error.required"))
      case dmy@DayMonthYear(_, _, _, _, _) => dateValidation(dmy)
  }

  def validDateOnly: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.validateDate") { dmy =>
    dateValidation(dmy)
  }

  private def dateTimeValidation(dmy: DayMonthYear): ValidationResult =   Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, dmy.hour.getOrElse(0), dmy.minutes.getOrElse(0))) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validDateTime: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
    case DayMonthYear(None, None, None, _, _) => Invalid(ValidationError("error.required"))
    case DayMonthYear(_, _, _, Some(h), None) => Invalid(ValidationError("error.invalid"))
    case DayMonthYear(_, _, _, None, Some(m)) => Invalid(ValidationError("error.invalid"))
    case dmy@DayMonthYear(_, _, _, _, _) => dateTimeValidation(dmy)
  }

  def validDateTimeOnly: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.validateDate") {
    case DayMonthYear(_, _, _, Some(h), None) => Invalid(ValidationError("error.invalid"))
    case DayMonthYear(_, _, _, None, Some(m)) => Invalid(ValidationError("error.invalid"))
    case dmy@DayMonthYear(_, _, _, _, _) => dateTimeValidation(dmy)
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

  def validPostcode: Constraint[String] = Constraint[String]("constraint.postcode") { postcode =>
    val postcodePattern = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) ?[0-9][A-Z]{2})$""".r

    postcodePattern.pattern.matcher(postcode).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.postcode"))
    }
  }

  def validPhoneNumber: Constraint[String] = Constraint[String]("constraint.phoneNumber") { phoneNumber =>
    val phoneNumberPattern = """[0-9 \-]{7,20}""".r

    phoneNumberPattern.pattern.matcher(phoneNumber).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.invalid"))
    }
  }

  /**
   * Use this method to validate phone number when it is not empty. This was created for fields which are mandatory and have to validate for
   * a valid phone number because the mandatory fields have their validation for empty fields.
   * @return
   */

  def validPhoneNumberRequired: Constraint[String] = Constraint[String]("constraint.phoneNumber") { phoneNumber =>
    val phoneNumberPattern = """[0-9 \-]{7,20}""".r

    if (null != phoneNumber && !phoneNumber.isEmpty){
        phoneNumberPattern.pattern.matcher(phoneNumber).matches match {
        case true => Valid
        case false => Invalid(ValidationError("error.invalid"))
      }
    } else {
      Valid
    }
  }



  def validDecimalNumberRequired: Constraint[String] = Constraint[String]("constraint.decimal") { decimal =>
    val decimalPattern = """^[0-9]{1,12}(\.[0-9]{1,2})?$""".r

    if(decimal != null && !decimal.isEmpty()) {
      decimalPattern.pattern.matcher(decimal).matches match {
        case true => Valid
        case false => Invalid(ValidationError("decimal.invalid"))
      }
    } else {
       Valid
    }
  }

  def validCurrencyRequired: Constraint[String] = Constraint[String]("constraint.currency") { decimal =>
    val decimalPattern = """^\£?[0-9]{1,12}(\.[0-9]{1,2})?$""".r

    validCurrencyWithPattern(decimalPattern, decimal)
  }

  def validCurrency5Required: Constraint[String] = Constraint[String]("constraint.currency") { decimal =>
    val decimalPattern = """^\£?[0-9]{1,5}(\.[0-9]{1,2})?$""".r

    validCurrencyWithPattern(decimalPattern, decimal)
  }

  def validCurrency8Required: Constraint[String] = Constraint[String]("constraint.currency") { decimal =>
    val decimalPattern = """^\£?[0-9]{1,8}(\.[0-9]{1,2})?$""".r
    validCurrencyWithPattern(decimalPattern, decimal)
  }

  private def validCurrencyWithPattern(decimalPattern:Regex, decimal: String):ValidationResult = {
    if(decimal != null && !decimal.isEmpty()) {
      decimalPattern.pattern.matcher(decimal).matches match {
        case true => Valid
        case false => Invalid(ValidationError("decimal.invalid"))
      }
    } else {
      Valid
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

  def validYesNoDontKnow: Constraint[String] = Constraint[String]("constraint.yesNoDontKnow") { answer =>
    answer match {
      case `yes` => Valid
      case `no` => Valid
      case `dontknow` => Valid
      case _ => Invalid(ValidationError("yesNo.invalid"))
    }
  }

  def paymentFrequencyValidation(pf: PaymentFrequency): ValidationResult = Try(new PaymentFrequency(pf.frequency, pf.other)) match {
    case Success(p: PaymentFrequency) if p.frequency == app.PensionPaymentFrequency.Other && p.other.isEmpty => Invalid(ValidationError("error.paymentFrequency"))
    case Success(p: PaymentFrequency) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def validPaymentFrequencyOnly: Constraint[PaymentFrequency] = Constraint[PaymentFrequency]("constraint.validatePaymentFrequency") {
    pf => paymentFrequencyValidation(pf)
  }

  def pensionPaymentFrequencyValidation(pf: PensionPaymentFrequency): ValidationResult = Try(new PensionPaymentFrequency(pf.frequency, pf.other)) match {
    case Success(p: PensionPaymentFrequency) if p.frequency.toLowerCase == "other" && p.other.isEmpty => {
      Invalid(ValidationError("error.paymentFrequency"))}
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

  def restrictedStringTextWithPound: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    // This is the same allowable characters as per the xml schema with some characters removed
    // The removed characters are : ()@<>
    val restrictedStringPattern = """^[A-Za-z0-9\s~!"#$%&£'\*\+,\-\./:;=\?\[\\\]_\{\}\^]*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.characters"))
    }
  }

}