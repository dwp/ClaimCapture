package controllers.s11_consent_and_declaration

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.{ FakeRequest, WithApplication }
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim

class G2ConsentSpec extends Specification with Tags {
  "Consent" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G2Consent.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result = G2Consent.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission (both no)" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("gettingInformationFromAnyEmployer.informationFromEmployer" -> "no",
          "gettingInformationFromAnyEmployer.why" -> "reason",
          "tellUsWhyEmployer.informationFromPerson" -> "no",
          "tellUsWhyEmployer.whyPerson" -> "reason")

      val result = G2Consent.submit(request)
      redirectLocation(result) must beSome("/consent-and-declaration/disclaimer")
    }

    "redirect to the next page after a valid submission (both yes)" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("gettingInformationFromAnyEmployer.informationFromEmployer" -> "yes",
          "tellUsWhyEmployer.informationFromPerson" -> "yes")

      val result = G2Consent.submit(request)
      redirectLocation(result) must beSome("/consent-and-declaration/disclaimer")
    }
  } section ("unit", models.domain.ConsentAndDeclaration.id)
}