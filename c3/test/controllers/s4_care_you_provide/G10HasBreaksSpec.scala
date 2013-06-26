package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import models.view.Claiming
import play.api.test.Helpers._

class G10HasBreaksSpec extends Specification {
  "Has breaks" should {
    """present "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G10HasBreaks.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G10HasBreaks.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept "yes" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "yes")

      val result = G10HasBreaks.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/breaksInCare")
    }

    """accept "no" to "Have you had any breaks in caring for this person" """ in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "no")

      val result = G10HasBreaks.submit(request)
      redirectLocation(result) must beSome("/careYouProvide/completed")
    }
  }
}