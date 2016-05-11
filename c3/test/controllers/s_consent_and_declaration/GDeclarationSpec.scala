package controllers.s_consent_and_declaration

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication
import play.api.Play.current

class GDeclarationSpec extends Specification {
  section("unit", models.domain.ConsentAndDeclaration.id)
  "Declaration" should {
    "present" in new WithApplication with Claiming {
      val gDeclaration = current.injector.instanceOf[GDeclaration]
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = gDeclaration.present(request)
      status(result) mustEqual OK
    }

    """enforce answer""" in new WithApplication with Claiming {
      val gDeclaration = current.injector.instanceOf[GDeclaration]
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = gDeclaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """failed filling nameOrOrganisation""" in new WithApplication with Claiming {
      val gDeclaration = current.injector.instanceOf[GDeclaration]
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = gDeclaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """failed filling why not contact person reason""" in new WithApplication with Claiming {
      val gDeclaration = current.injector.instanceOf[GDeclaration]
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("tellUsWhyFromAnyoneOnForm.informationFromPerson" -> "no")

      val result = gDeclaration.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
  section("unit", models.domain.ConsentAndDeclaration.id)
}
