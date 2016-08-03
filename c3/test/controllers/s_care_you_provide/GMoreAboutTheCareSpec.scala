package controllers.s_care_you_provide

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class GMoreAboutTheCareSpec extends Specification {
  val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no", "otherCarer" -> "no")

  section("unit", models.domain.CareYouProvide.id)
  "More about the care" should {
    """present More about the care""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GMoreAboutTheCare.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no input" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "success for minimal input without optional fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)

      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "error if not enter select other-carer" in new WithApplication with Claiming {
      val input = Seq("spent35HoursCaring" -> "no")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "error if other-carer and not select other-carer-gets-uc" in new WithApplication with Claiming {
      val input = Seq("spent35HoursCaring" -> "no", "otherCarer" -> "yes")
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(input: _*)
      val result = GMoreAboutTheCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  }
  section("unit", models.domain.CareYouProvide.id)
}
