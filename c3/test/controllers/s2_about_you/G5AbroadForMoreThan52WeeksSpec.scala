package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim

class G5AbroadForMoreThan52WeeksSpec extends Specification with Tags {
  "Abroad more than 52 weeks" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G5AbroadForMoreThan52Weeks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Details of time abroad for more than 52 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G5AbroadForMoreThan52Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Details of time abroad for more than 52 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")

      val result = G5AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome("/about-you/trip/52-weeks")
    }

    """accept "no" to "Details of time abroad for more than 52 weeks".""" in new WithApplication with Claiming {
      pending
      /*val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey).withFormUrlEncodedBody("anyTrips" -> "no")

      val result = G2AbroadForMoreThan4Weeks.submit(request)
      redirectLocation(result) must beSome("/other-eea-state-switzerland")*/
    }

    "complete upon indicating that there are no more 52 week trips having provided zero trip details" in new WithApplication with Claiming {
      pending
    }

    "complete upon indicating that there are no more 52 week trips having now provided one trip" in new WithApplication with Claiming {
      pending
    }

    "have no trips upon deleting a 52 week trip" in new WithApplication with Claiming {
      pending
    }
  } section("unit", models.domain.AboutYou.id)
}
