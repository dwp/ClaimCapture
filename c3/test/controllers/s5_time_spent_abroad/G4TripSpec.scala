package controllers.s5_time_spent_abroad

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Trips, Claiming, Claim}

class G4TripSpec extends Specification {
  "4 week trip" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G4Trip.fourWeeks(request)
      status(result) mustEqual OK
    }

    "be rejected for missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G4Trip.fourWeeksSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add two 4 week trips" in new WithApplication with Claiming {
      val request1 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "tripID" -> "1",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "end.day" -> "1",
        "end.month" -> "1",
        "end.year" -> "2001",
        "where" -> "Scotland",
        "why" -> "For the sun")

      G4Trip.fourWeeksSubmit(request1)

      val request2 = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "tripID" -> "2",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "end.day" -> "1",
        "end.month" -> "1",
        "end.year" -> "2001",
        "where" -> "Greenland")

      G4Trip.fourWeeksSubmit(request2)

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fourWeeksTrips.size shouldEqual 2
      }
    }

    "update existing trip" in new WithApplication with Claiming {
      val tripID = "1"

      val requestNew = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "tripID" -> tripID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "end.day" -> "1",
        "end.month" -> "1",
        "end.year" -> "2001",
        "where" -> "Greenland")

      G4Trip.fourWeeksSubmit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "tripID" -> tripID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> yearUpdate.toString,
        "end.day" -> "1",
        "end.month" -> "1",
        "end.year" -> "2001",
        "where" -> "Greenland")

      G4Trip.fourWeeksSubmit(requestUpdate)

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fourWeeksTrips.head.start.year must beSome(yearUpdate)
      }
    }

    "allow no more than 5 four week trips" in new WithApplication with Claiming {
      for (index <- 1 to 5) {
        val request = FakeRequest().withSession("connected" -> claimKey)
          .withFormUrlEncodedBody(
          "tripID" -> index.toString,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Greenland")

        G4Trip.fourWeeksSubmit(request)
      }

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fourWeeksTrips.size shouldEqual 5
      }

      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "tripID" -> "TOO MANY",
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "end.day" -> "1",
        "end.month" -> "1",
        "end.year" -> "2001",
        "where" -> "Greenland")

      val result = G4Trip.fourWeeksSubmit(request)
      redirectLocation(result) must beSome("/timeSpentAbroad/abroadForMoreThan4Weeks")

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fourWeeksTrips.size shouldEqual 5
      }
    }
  }
}