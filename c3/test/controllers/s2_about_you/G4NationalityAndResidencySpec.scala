package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain.Claiming
import models.view.CachedClaim
import play.api.test.Helpers._

class G4NationalityAndResidencySpec extends Specification with Tags {

  val aboutYourMinimalInput = Seq("nationality" -> "British", "resideInUK.answer" -> "yes")
  val aboutYourWithOptionalInput = Seq("nationality" -> "British", "resideInUK.answer" -> "no", "resideInUK.text" -> "Maldives")
  val aboutYourWithMissingOptionalInput = Seq("nationality" -> "British", "resideInUK.answer" -> "no")

  "Your nationality and residency" should {
    """present Your nationality and residency""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4NationalityAndResidency.present(request)
      status(result) mustEqual OK
    }

    """fail submit for no input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """success for minimal input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(aboutYourMinimalInput: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for minimal input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(aboutYourWithOptionalInput: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """fail submit for missing residency""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(aboutYourWithMissingOptionalInput: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.AboutYou.id)
}
