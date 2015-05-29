package controllers.s10_information

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class G1AdditionalInformationSpec extends Specification with Tags {

  val validYesInput = Seq(
    "anythingElse.answer" -> "yes",
    "anythingElse.text" -> "Additional info text",
    "welshCommunication" -> "yes"
  )

  "Additional information" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1AdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to all questions""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1AdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept answers""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody(validYesInput: _*)

      val result = G1AdditionalInfo.submit(request)
      redirectLocation(result) must beSome("/preview")
    }
  } section("unit", models.domain.ConsentAndDeclaration.id)
}