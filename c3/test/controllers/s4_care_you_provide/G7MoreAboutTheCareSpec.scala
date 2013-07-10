package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G7MoreAboutTheCareSpec extends Specification with Mockito with Tags {

  val moreAboutTheCareInput = Seq("spent35HoursCaring" -> "no", "spent35HoursCaringBeforeClaim" -> "no", "hasSomeonePaidYou" -> "no")

  "More about the care" should {

    """present More about the care""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G7MoreAboutTheCare.present(request)
      status(result) mustEqual OK
    }

    "fail submit for no input" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G7MoreAboutTheCare.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "success for minimal input without optional fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody(moreAboutTheCareInput: _*)

      val result = controllers.s4_care_you_provide.G7MoreAboutTheCare.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"

}
