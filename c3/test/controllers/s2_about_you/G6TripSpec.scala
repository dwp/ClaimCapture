package controllers.s2_about_you

import models.domain.{Claiming, Trips}
import models.view.CachedClaim
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G6TripSpec extends Specification with Tags {
  "52 week trip" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G6Trip.present(request)
      status(result) mustEqual OK
    }

    "be rejected for missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G6Trip.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "add two 52 week trips" in new WithApplication with Claiming {
      val request1 = FakeRequest()
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

      val result1 = G6Trip.submit(request1)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
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

      G6Trip.submit(request2)

      getClaimFromCache(result1).questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fiftyTwoWeeksTrips.size shouldEqual 2
      }
    }

    "update existing trip" in new WithApplication with Claiming {
      val tripID = "1"

      val requestNew = FakeRequest()
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

      val result1 = G6Trip.submit(requestNew)

      val yearUpdate = 2005

      val requestUpdate = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
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

      G6Trip.submit(requestUpdate)

      getClaimFromCache(result1).questionGroup(Trips) must beLike {
        case Some(t: Trips) => t.fiftyTwoWeeksTrips.head.start.get.year must beSome(yearUpdate)
      }
    }

    "allow no more than 5 four week trips" in new WithApplication with Claiming {
      val request1 = FakeRequest()
        .withFormUrlEncodedBody(
          "tripID" -> "1",
          "start.day" -> "1",
          "start.month" -> "1",
          "start.year" -> "2001",
          "end.day" -> "1",
          "end.month" -> "1",
          "end.year" -> "2001",
          "where" -> "Greenland",
          "why_reason" -> "Holiday",
          "personWithYou" -> "yes")

      val result1 = G6Trip.submit(request1)

      for (index <- 2 to 5) {
        val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
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

        G6Trip.submit(request)
      }

      getClaimFromCache(result1).questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fiftyTwoWeeksTrips.size shouldEqual 5
      }

      val request = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
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

      val result2 = G6Trip.submit(request)
      redirectLocation(result2) must beSome("/about-you/abroad-for-more-than-52-weeks")

      getClaimFromCache(result2).questionGroup(Trips) must beLike {
        case Some(ts: Trips) => ts.fiftyTwoWeeksTrips.size shouldEqual 6
      }
    }

    "issue an 'error' when deleting a non-existing job" in new WithApplication with Claiming {
      val result = G6Trip.delete("nonExistingTripID")(FakeRequest())
      status(result) shouldEqual BAD_REQUEST
    }
  } section ("unit", models.domain.AboutYou.id)
}
