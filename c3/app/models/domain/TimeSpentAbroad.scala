package models.domain

import models.DayMonthYear

case object TimeSpentAbroad {
  val id = "s5"
}

case class NormalResidenceAndCurrentLocation(normallyLiveInUK: String, whereDoYouNormallyLive: Option[String] = None, inGBNow: String) extends QuestionGroup(NormalResidenceAndCurrentLocation.id)

case object NormalResidenceAndCurrentLocation extends QuestionGroup(s"${TimeSpentAbroad.id}.g1")

case class Trips(fourWeeksTrips: List[FourWeeksTrip] = Nil, fiftyTwoWeeksTrips: List[FiftyTwoWeeksTrip] = Nil) extends QuestionGroup(Trips.id) {
  def update(trip: FourWeeksTrip) = {
    val updated = fourWeeksTrips map {
      t => if (t.id == trip.id) trip else t
    }

    if (updated.contains(trip)) Trips(updated, fiftyTwoWeeksTrips) else Trips(fourWeeksTrips :+ trip, fiftyTwoWeeksTrips)
  }

  def update(trip: FiftyTwoWeeksTrip) = {
    val updated = fiftyTwoWeeksTrips map {
      t => if (t.id == trip.id) trip else t
    }

    if (updated.contains(trip)) Trips(fourWeeksTrips, updated) else Trips(fourWeeksTrips, fiftyTwoWeeksTrips :+ trip)
  }

  def delete(tripID: String) = Trips(fourWeeksTrips.filterNot(_.id == tripID), fiftyTwoWeeksTrips.filterNot(_.id == tripID))
}

case object Trips extends QuestionGroup(s"${TimeSpentAbroad.id}.g4") {
  def apply() = new Trips()
}

case class Trip(val id: String, start: DayMonthYear, end: DayMonthYear, where: String, why: Option[String] = None) extends FourWeeksTrip with FiftyTwoWeeksTrip {
  def as[T >: Trip]: T = asInstanceOf[T]
}

trait FourWeeksTrip {
  this: Trip =>

  val id: String
}

trait FiftyTwoWeeksTrip {
  this: Trip =>

  val id: String
}