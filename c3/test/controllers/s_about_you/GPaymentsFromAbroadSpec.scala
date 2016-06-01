package controllers.s_about_you

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.{PaymentsFromAbroad, Claiming}
import utils.WithApplication

class GPaymentsFromAbroadSpec extends Specification {
  section("unit", models.domain.PaymentsFromAbroad.id)
  "Other EEA State of Switzerland" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GPaymentsFromAbraod.present(request)
      status(result) mustEqual OK
    }

    "return bad request on invalid data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GPaymentsFromAbraod.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """be added to cached claim upon answering "no" to "benefits from other EEA state or Switzerland".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("eeaGuardQuestion.answer" -> "no")

      val result = GPaymentsFromAbraod.submit(request)

      val claim = getClaimFromCache(result)

      claim.questionGroup(PaymentsFromAbroad) must beLike {
        case Some(o: PaymentsFromAbroad) => {
          o.guardQuestion.answer shouldEqual "no"
        }
      }
    }
  }
  section("unit", models.domain.PaymentsFromAbroad.id)
}
