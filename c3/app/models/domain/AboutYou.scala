package models.domain

import models.{ReasonForBeingThere, NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.yesNo.{YesNo, YesNoWithText}
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object AboutYou extends Section.Identifier {
  val id = "s3"
}

case class YourDetails(title: String = "",
                       firstName: String = "",
                       middleName: Option[String] = None,
                       surname: String = "",
                       otherSurnames: Option[String] = None,
                       nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(None,None,None,None,None),
                       dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)) extends QuestionGroup(YourDetails) {

  def otherNames = firstName + middleName.map(" " + _).getOrElse("")
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: MultiLineAddress = new MultiLineAddress(),
                          postcode: Option[String] = None,
                          howWeContactYou: String = "",
                          contactYouByTextphone: Option[String] = None) extends QuestionGroup(ContactDetails)

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class NationalityAndResidency(nationality: String,
                                   actualnationality: Option[String] = None,
                                   resideInUK: YesNoWithText = YesNoWithText("", None),
                                   maritalStatus: Option[String] = None) extends QuestionGroup(NationalityAndResidency)

object NationalityAndResidency extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"

  val british = "British"
  val anothercountry = "Another Country"

  def validNationality: Constraint[String] = Constraint[String]("constraint.nationality") { answer =>
    // Nationality is a radio list with two possible values, British and Another Country
    answer match {
      case `british` => Valid
      case `anothercountry` => Valid
      case _ => Invalid(ValidationError("nationality.invalid"))
    }
  }

  def actualNationalityRequired: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.actualnationality") { nationalityAndResidency =>
    // if the Nationality is Another Country the user must provide their National Residency
    if (nationalityAndResidency.nationality == anothercountry) {
      nationalityAndResidency.actualnationality match {
        case Some(place) => Valid
        case _ => Invalid(ValidationError("actualnationality.required"))
      }
    }
    else Valid
  }

  def maritalStatusRequired: Constraint[NationalityAndResidency] = Constraint[NationalityAndResidency]("constraint.actualnationality") { nationalityAndResidency =>
    // if the Nationality is Another Country the user must provide their National Residency
    if (nationalityAndResidency.nationality == anothercountry) {
      nationalityAndResidency.maritalStatus match {
        case Some(maritalStatus) => Valid
        case _ => Invalid(ValidationError("maritalstatus.required"))
      }
    }
    else Valid
  }

  def validateHadPartner(nationalityAndResidency: NationalityAndResidency) = nationalityAndResidency.maritalStatus == "p"
}

case class AbroadForMoreThan52Weeks(anyTrips: String = "") extends QuestionGroup(AbroadForMoreThan52Weeks)

object AbroadForMoreThan52Weeks extends QuestionGroup.Identifier  {
  val id = s"${AboutYou.id}.g5"
}

case class Trips(fiftyTwoWeeksTrips: List[FiftyTwoWeeksTrip] = Nil) extends QuestionGroup(Trips) {
  def update(trip: FiftyTwoWeeksTrip): Trips = {
    val updated = fiftyTwoWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(updated) else Trips(fiftyTwoWeeksTrips :+ trip)
  }

  def +(trip: FiftyTwoWeeksTrip): Trips = update(trip)

  def delete(tripID: String): Trips = Trips(fiftyTwoWeeksTrips.filterNot(_.id == tripID))

  def -(tripID: String): Trips = delete(tripID)
}

object Trips extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g6"
}

case class Trip(id: String, where: String, start: Option[DayMonthYear] = None, end: Option[DayMonthYear] = None, why: Option[ReasonForBeingThere], personWithYou: String) extends FiftyTwoWeeksTrip {
  def as[T >: Trip]: T = asInstanceOf[T]
}

sealed trait TripPeriod {
  val id: String

  val where: String

  val start: Option[DayMonthYear]

  val end: Option[DayMonthYear]

  val why: Option[ReasonForBeingThere]

  val personWithYou: String
}

trait FiftyTwoWeeksTrip extends TripPeriod {
  this: Trip =>
}

case class OtherEEAStateOrSwitzerland(benefitsFromEEA: String = "", workingForEEA: String = "") extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g7"
}

