package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim

class G4DeclarationSpec extends Specification with Tags {
  "Declaration" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4Declaration.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4Declaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """failed filling nameOrOrganisation""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("confirm" -> "checked","someoneElse" -> "checked")

      val result = G4Declaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers without someoneElse""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("confirm" -> "checked")

      val result = G4Declaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("confirm" -> "checked","nameOrOrganisation"->"SomeOrg","someoneElse" -> "checked")

      val result = G4Declaration.submit(request)
      redirectLocation(result) must beSome("/consent-and-declaration/submit")
    }
  } section("unit", models.domain.ConsentAndDeclaration.id)
}