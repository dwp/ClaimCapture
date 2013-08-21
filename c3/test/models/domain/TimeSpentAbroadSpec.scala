package models.domain

import org.specs2.mutable.Specification
import models.DayMonthYear

class TimeSpentAbroadSpec extends Specification {
  "Trips" should {
    """add 2 new "four weeks" trips.""" in {
      val trips = Trips()

      val updatedTrips = trips
        .update(Trip("1", DayMonthYear(1, 1, 2000), DayMonthYear(1, 1, 2000), "Scotland", "For Holiday").as[FourWeeksTrip])
        .update(Trip("2", DayMonthYear(1, 1, 2000), DayMonthYear(1, 1, 2000), "Greenland", "For Holiday").as[FourWeeksTrip])

      trips.fourWeeksTrips.size mustEqual 0
      trips.fiftyTwoWeeksTrips.size mustEqual 0

      updatedTrips.fourWeeksTrips.size mustEqual 2
      updatedTrips.fiftyTwoWeeksTrips.size mustEqual 0
    }
  }
}