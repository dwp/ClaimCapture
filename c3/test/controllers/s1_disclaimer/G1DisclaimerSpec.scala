package controllers.s1_disclaimer

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class G1DisclaimerSpec extends Specification with Tags {
  "Disclaimer" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1Disclaimer.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1Disclaimer.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("read" -> "yes")

      val result = G1Disclaimer.submit(request)
      redirectLocation(result) must beSome("/your-claim-date/claim-date")
    }
  } section("unit", models.domain.DisclaimerSection.id)
}