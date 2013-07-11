package models.domain

import models.DayMonthYear
import models.yesNo.YesNoWithText

case object TimeSpentAbroad {
  val id = "s5"
}

case class NormalResidenceAndCurrentLocation(whereDoYouLive:YesNoWithText, inGBNow: String) extends QuestionGroup(NormalResidenceAndCurrentLocation.id)

case object NormalResidenceAndCurrentLocation extends QuestionGroup(s"${TimeSpentAbroad.id}.g1")

case object AbroadForMoreThan4Weeks extends QuestionGroup(s"${TimeSpentAbroad.id}.g2")

case object AbroadForMoreThan52Weeks extends QuestionGroup(s"${TimeSpentAbroad.id}.g3")

case class Trips(fourWeeksTrips: List[FourWeeksTrip] = Nil, fiftyTwoWeeksTrips: List[FiftyTwoWeeksTrip] = Nil) extends QuestionGroup(Trips.id) {
  def update(trip: FourWeeksTrip) = {
    val updated = fourWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(updated, fiftyTwoWeeksTrips) else Trips(fourWeeksTrips :+ trip, fiftyTwoWeeksTrips)
  }

  def update(trip: FiftyTwoWeeksTrip) = {
    val updated = fiftyTwoWeeksTrips map { t => if (t.id == trip.id) trip else t }
    if (updated.contains(trip)) Trips(fourWeeksTrips, updated) else Trips(fourWeeksTrips, fiftyTwoWeeksTrips :+ trip)
  }

  def delete(tripID: String) = Trips(fourWeeksTrips.filterNot(_.id == tripID), fiftyTwoWeeksTrips.filterNot(_.id == tripID))
}

case object Trips extends QuestionGroup(s"${TimeSpentAbroad.id}.g4") {
  def apply() = new Trips()
}

case class Trip(id: String, start: DayMonthYear, end: DayMonthYear, where: String, why: Option[String] = None) extends FourWeeksTrip with FiftyTwoWeeksTrip {
  def as[T >: Trip]: T = asInstanceOf[T]
}

sealed trait TripPeriod {
  val id: String

  val start: DayMonthYear

  val end: DayMonthYear

  val where: String

  val why: Option[String]
}

trait FourWeeksTrip extends TripPeriod {
  this: Trip =>
}

trait FiftyTwoWeeksTrip extends TripPeriod {
  this: Trip =>
}