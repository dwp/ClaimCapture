package controllers.s_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain._
import models.view.CachedClaim
import play.api.cache.Cache
import models.domain.Claim
import utils.WithApplication
import scala.Some

class GAbroadForMoreThan52WeeksSpec extends Specification with Tags {

  val tripsPage = "/about-you/trip/52-weeks"
  val eeaPage = "/about-you/other-eea-state-or-switzerland"
  val timeAbroadPage = "/about-you/abroad-for-more-than-52-weeks"

  "Abroad more than 52 weeks" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GAbroadForMoreThan52Weeks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GAbroadForMoreThan52Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """enforce answer when "Time outside of England, Scotland or Wales" is "yes" """ in new WithApplication with Claiming{
      val request = FakeRequest().withFormUrlEncodedBody("anyTrips" -> "yes")

      val result = GAbroadForMoreThan52Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("anyTrips" -> "yes", "tripDetails" -> "Trip1 to London")

      val result = GAbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(eeaPage)
    }

    """accept "no" to "Time outside of England, Scotland or Wales".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("anyTrips" -> "no")

      val result = GAbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome(eeaPage)
    }

  } section("unit", models.domain.AboutYou.id)
}
