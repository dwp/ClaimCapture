package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import models.domain.Claiming
import models.view.CachedClaim
import play.api.test.Helpers._

class G4NationalityAndResidencySpec extends Specification with Tags {

  val inputBritish = Seq("nationality" -> "british")
  val inputAnotherCountry = Seq("nationality" -> "anothercountry", "residency" -> "French")
  val inputAnotherCountryMissingData = Seq("nationality" -> "anothercountry")
  
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

    """success for british input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputBritish: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for input with all another country""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountry: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """fail submit for missing residency""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountryMissingData: _*)

      val result = G4NationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.AboutYou.id)
}
