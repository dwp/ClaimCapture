package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import models.view.CachedClaim
import play.api.cache.Cache
import models.domain.Claim
import scala.Some

class G5AbroadForMoreThan52WeeksSpec extends Specification with Tags {

  val tripsPage = "/about-you/trip/52-weeks"
  val eeaPage = "/about-you/other-eea-state-or-switzerland"
  val timeAbroadPage = "/about-you/abroad-for-more-than-52-weeks"

  "Abroad more than 52 weeks" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G5AbroadForMoreThan52Weeks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G5AbroadForMoreThan52Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")

      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(tripsPage)
    }

    """accept "no" to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "no")

      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(eeaPage)
    }

    "go to Other EEA state or switzerland page having now provided less than 5 trips and answering 'no' to Time outside of England, Scotland or Wales" in new WithApplication with Claiming {
      createTrip(claimKey, "12345")
      verifyTrips(claimKey, 1)

      val requestNoTrips = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "no")
      val resultNoTrips = G5AbroadForMoreThan52Weeks.submit(requestNoTrips)
      redirectLocation(resultNoTrips) must beSome(eeaPage)
    }

    "go to trips page having now provided less than 5 trips and answering 'yes' to Time outside of England, Scotland or Wales" in new WithApplication with Claiming {
      for (i <- 1 to 4){
        createTrip(claimKey, "12345"+i)
      }
      verifyTrips(claimKey, 4)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")
      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(tripsPage)

    }

    "stay on Time outside of England, Scotland or Wales page having now provided 5 trips and answering 'yes' to Time outside of England, Scotland or Wales" in new WithApplication with Claiming {
      for (i <- 1 to 5){
        createTrip(claimKey, "12345"+i)
      }
      verifyTrips(claimKey, 5)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")
      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(timeAbroadPage)

    }

    "go to Other EEA state or switzerland page deleting existing trip and answering 'no' to Time outside of England, Scotland or Wales" in new WithApplication with Claiming {
      createTrip(claimKey, "12345")
      verifyTrips(claimKey, 1)

      G6Trip.delete("12345")(FakeRequest().withSession(CachedClaim.key -> claimKey))
      verifyTrips(claimKey, 0)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "no")
      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(eeaPage)
    }

    def verifyTrips (claimKey:String, noOfTrips:Int) = {
      import play.api.Play.current

      Cache.getAs[Claim](claimKey).get.questionGroup(Trips) should beLike {
        case Some(t: Trips) => t.fiftyTwoWeeksTrips.size shouldEqual noOfTrips
      }
    }


    def createTrip(claimKey: String, tripId: String) = {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")
      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(tripsPage)

      val tripRequest = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("tripID" -> tripId,
        "where" -> "USA",
        "start.day" -> "01",
        "start.month" -> "01",
        "start.year" -> "2002",
        "end.day" -> "01",
        "end.month" -> "01",
        "end.year" -> "2003",
        "why" -> "Holiday",
        "personWithYou" -> "yes")
      val tripResult = G6Trip.submit(tripRequest)
      redirectLocation(tripResult) must beSome(timeAbroadPage)
    }

  } section("unit", models.domain.AboutYou.id)
}
