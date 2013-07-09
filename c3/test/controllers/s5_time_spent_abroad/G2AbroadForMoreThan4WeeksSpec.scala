package controllers.s5_time_spent_abroad

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G2AbroadForMoreThan4WeeksSpec extends Specification {
  "Normal residence and current location" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2AbroadForMoreThan4Weeks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "abroad for more than 4 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2AbroadForMoreThan4Weeks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "abroad for more than 4 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "yes")

      val result = G2AbroadForMoreThan4Weeks.submit(request)
      redirectLocation(result) must beSome("/timeSpentAbroad/trip/4Weeks")
    }

    """accept "no" to "abroad for more than 4 weeks".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result = G2AbroadForMoreThan4Weeks.submit(request)
      redirectLocation(result) must beSome("/timeSpentAbroad/abroadForMoreThan52Weeks")
    }

    "complete upon indicating that there are no more 4 week trips having provided zero trip details" in new WithApplication with Claiming {
      pending
    }

    "complete upon indicating that there are no more 4 week trips having now provided one trip" in new WithApplication with Claiming {
      pending
    }

    "allow no more than 10 trips" in new WithApplication with Claiming {
      pending
    }

    "have no trips upon deleting a 4 week trip" in new WithApplication with Claiming {
      pending
    }
  }
}