package controllers.s_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import models.domain.Claiming
import models.view.CachedClaim
import play.api.test.Helpers._
import utils.WithApplication

class GNationalityAndResidencySpec extends Specification with Tags {

  val inputBritish = Seq("nationality" -> "British", "resideInUK.answer" -> "yes")
  val inputAnotherCountry = Seq("nationality" -> "Another Country", "actualnationality" -> "French", "resideInUK.answer" -> "yes", "maritalStatus" -> "Single")
  val inputBritishResideOutside = Seq("nationality" -> "British", "resideInUK.answer" -> "no", "resideInUK.text" -> "Maldives")
  val inputAnotherCountryResideOutside = Seq("nationality" -> "Another Country", "actualnationality" -> "French", "resideInUK.answer" -> "no", "resideInUK.text" -> "Maldives", "maritalStatus" -> "Single")
  val inputAnotherCountryMissingData = Seq("nationality" -> "Another Country", "resideInUK.answer" -> "yes")
  val inputAnotherCountryMissingData2 = Seq("nationality" -> "British", "resideInUK.answer" -> "no")

  "Your nationality and residency" should {
    """present Your nationality and residency""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GNationalityAndResidency.present(request)
      status(result) mustEqual OK
    }

    """fail submit for no input""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """success for British input residing inside the UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputBritish: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for British input residing outside the UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputBritishResideOutside: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for input with another country residing the UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountry: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """success for input with another country outsiding the UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountryResideOutside: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """fail submit for missing actualnationality""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountryMissingData: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """fail submit for missing residence outside UK""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(inputAnotherCountryMissingData2: _*)

      val result = GNationalityAndResidency.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.AboutYou.id)
}
