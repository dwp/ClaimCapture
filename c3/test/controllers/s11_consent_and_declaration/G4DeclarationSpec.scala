package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.{Declaration, Claiming}
import models.view.CachedClaim
import services.submission.MockInjector

class G4DeclarationSpec extends Specification with MockInjector with Tags {

  val G4Declaration = resolve(classOf[G4Declaration])

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
      status(result) mustEqual SEE_OTHER
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("confirm" -> "checked","nameOrOrganisation"->"SomeOrg","someoneElse" -> "checked")

      val result = G4Declaration.submit(request)

      val claim = getClaimFromCache(result, CachedClaim.key)

      claim.questionGroup[Declaration] must beLike {
        case Some(f: Declaration) =>
          f.nameOrOrganisation must equalTo(Some("SomeOrg"))
          f.read must equalTo("checked")
          f.someoneElse.get must equalTo("checked")
      }

      redirectLocation(result) must beSome("/async-submitting")
    }
  } section("unit", models.domain.ConsentAndDeclaration.id)
}