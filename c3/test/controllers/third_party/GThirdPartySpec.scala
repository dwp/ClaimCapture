package controllers.third_party

import models.domain.Claiming
import models.view.CachedClaim
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GThirdPartySpec extends Specification {
  val inputCarer = Seq("thirdParty" -> "yesCarer")
  val inputNoCarerOrganisation = Seq("thirdParty" -> "noCarer", "thirdParty.nameAndOrganisation" -> "Test")

  section("unit", models.domain.ThirdParty.id)
  "Third party" should {
    """present are you applying for""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GThirdParty.present(request)
      status(result) mustEqual OK
    }

    """fail submit for no input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GThirdParty.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """success for yesCarer input residing inside the UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputCarer: _*)

      val result = GThirdParty.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for noCarer input name and organisation""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputNoCarerOrganisation: _*)

      val result = GThirdParty.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
  section("unit", models.domain.ThirdParty.id)
}
