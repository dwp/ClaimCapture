package controllers.s2_about_you

import org.specs2.mutable.{Specification,Tags}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Trips, Claiming, Claim}
import models.view.CachedClaim

class G6TripSpec extends Specification with Tags {
  "52 week trip" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G6Trip.fiftyTwoWeeks(request)
      status(result) mustEqual OK
    }

    "be rejected for missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G6Trip.fiftyTwoWeeksSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add two 52 week trips" in new WithApplication with Claiming {
      val request1 = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
          "tripID" -> "1",
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Scotland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      G6Trip.fiftyTwoWeeksSubmit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
          "tripID" -> "2",
          "start.day" -> "2",
          "start.month" -> "2",
          "start.year" -> "2002",
          "end.day" -> "2",
          "end.month" -> "2",
          "end.year" -> "2002",
          "where" -> "Greenland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      G6Trip.fiftyTwoWeeksSubmit(request2)

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fiftyTwoWeeksTrips.size shouldEqual 2
      }
    }

    "update existing trip" in new WithApplication with Claiming {
      val tripID = "1"

      val requestNew = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
          "tripID" -> tripID,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Greenland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      G6Trip.fiftyTwoWeeksSubmit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
          "tripID" -> tripID,
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> yearUpdate.toString,
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Greenland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      G6Trip.fiftyTwoWeeksSubmit(requestUpdate)

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fiftyTwoWeeksTrips.head.start.get.year must beSome(yearUpdate)
      }
    }

    "allow no more than 5 four week trips" in new WithApplication with Claiming {
      for (index <- 1 to 5) {
        val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
          .withFormUrlEncodedBody(
            "tripID" -> index.toString,
            "start.day" -> "1",
            "start.month" -> "1",
            "start.year" -> "2001",
            "end.day" -> "1",
            "end.month" -> "1",
            "end.year" -> "2001",
            "where" -> "Greenland",
            "why_reason" -> "Holiday",
            "personWithYou" -> "yes")

        G6Trip.fiftyTwoWeeksSubmit(request)
      }

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fiftyTwoWeeksTrips.size shouldEqual 5
      }

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
          "tripID" -> "TOO MANY",
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Greenland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      val result = G6Trip.fiftyTwoWeeksSubmit(request)
      redirectLocation(result) must beSome("/about-you/abroad-for-more-than-52-weeks")

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fiftyTwoWeeksTrips.size shouldEqual 5
      }
    }

    "issue an 'error' when deleting a non-existing job" in new WithApplication with Claiming {
      val result = G6Trip.delete("nonExistingTripID")(FakeRequest().withSession(CachedClaim.key -> claimKey))
      status(result) shouldEqual BAD_REQUEST
    }
  } section ("unit", models.domain.AboutYou.id)
}
