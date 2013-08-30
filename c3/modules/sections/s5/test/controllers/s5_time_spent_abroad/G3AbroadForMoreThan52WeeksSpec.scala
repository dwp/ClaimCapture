package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim

class G3AbroadForMoreThan52WeeksSpec extends Specification with Tags {
  "Abroad more than 52 weeks" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G3AbroadForMoreThan52Weeks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "abroad for more than 52 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G3AbroadForMoreThan52Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "abroad for more than 52 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey).withFormUrlEncodedBody("anyTrips" -> "yes")

      val result = G3AbroadForMoreThan52Weeks.submit(request)
      redirectLocation(result) must beSome("/trip/52-weeks")
    }

    """accept "no" to "abroad for more than 52 weeks".""" in new WithApplication with Claiming {
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
  } section("unit", models.domain.TimeSpentAbroad.id)
}