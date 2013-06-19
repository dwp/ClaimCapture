package models.claim

import models.DayMonthYear

object CareYouProvide {
  val id = "s4"
}

case class TheirPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                                otherNames: Option[String], nationalInsuranceNumber: Option[String],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends Form(TheirPersonalDetails.id)

object TheirPersonalDetails {
  val id = s"${CareYouProvide.id}.g1"
}

case class Breaks(breaks: String) extends Form(Breaks.id)

object Breaks {
  val id = s"${CareYouProvide.id}.g9"
}

case class BreaksInCare(moreBreaks: String) extends Form(BreaksInCare.id)

object BreaksInCare {
  val id = s"${CareYouProvide.id}.g10"
}