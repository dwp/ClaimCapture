package controllers.s_information

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class GAdditionalInformationSpec extends Specification {
  val validYesInput = Seq(
    "anythingElse.answer" -> "yes",
    "anythingElse.text" -> "Additional info text",
    "welshCommunication" -> "yes"
  )

  section("unit", models.domain.AdditionalInfo.id)
  "Additional information" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GAdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GAdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody(validYesInput: _*)

      val result = GAdditionalInfo.submit(request)
      redirectLocation(result) must beSome("/preview")
    }
  }
  section("unit", models.domain.AdditionalInfo.id)
}
