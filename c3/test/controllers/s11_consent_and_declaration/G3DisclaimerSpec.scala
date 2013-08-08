package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G3DisclaimerSpec extends Specification with Tags {
  "Disclaimer" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G3Disclaimer.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G3Disclaimer.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
                                 .withFormUrlEncodedBody("read" -> "yes")

      val result = G3Disclaimer.submit(request)
      redirectLocation(result) must beSome("/consentAndDeclaration/declaration")
    }
  } section("unit",models.domain.ConsentAndDeclaration.id)
}