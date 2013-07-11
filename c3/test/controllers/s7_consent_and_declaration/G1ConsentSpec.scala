package controllers.s7_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G1ConsentSpec extends Specification with Tags {
  "Consent" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1Consent.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G1Consent.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
                                 .withFormUrlEncodedBody("informationFromEmployer" -> "yes",
                                                         "why"->"reason",
                                                         "informationFromPerson"->"yes")

      val result = G1Consent.submit(request)
      redirectLocation(result) must beSome("/consentAndDeclaration/disclaimer")
    }
  } section "unit"
}