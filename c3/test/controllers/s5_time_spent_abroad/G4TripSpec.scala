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

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fourWeeksTrips.size mustEqual 2
      }
    }

    /*
    "update existing break" in new WithApplication with Claiming {
      val breakID = "1"

      val requestNew = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> "2001",
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "breakID" -> breakID,
        "start.day" -> "1",
        "start.month" -> "1",
        "start.year" -> yearUpdate.toString,
        "whereYou.location" -> "Holiday",
        "wherePerson.location" -> "Holiday",
        "medicalDuringBreak" -> "no")

      G11Break.submit(requestUpdate)

      Cache.getAs[Claim](claimKey).get.questionGroup(BreaksInCare) must beLike {
        case Some(b: BreaksInCare) =>
          b.breaks.head.start.year must beSome(yearUpdate)
      }
    }*/
  }
}