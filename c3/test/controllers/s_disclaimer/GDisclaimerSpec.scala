package controllers.s_disclaimer

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class GDisclaimerSpec extends Specification {
  section("unit", models.domain.DisclaimerSection.id)
  "Disclaimer" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GDisclaimer.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GDisclaimer.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("read" -> "yes")

      val result = GDisclaimer.submit(request)
      redirectLocation(result) must beSome("/your-claim-date/claim-date")
    }
  }
  section("unit", models.domain.DisclaimerSection.id)
}
