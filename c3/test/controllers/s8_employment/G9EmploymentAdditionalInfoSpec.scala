package controllers.s8_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import models.domain.Claiming
import play.api.test.Helpers._
import models.view.CachedClaim
import utils.WithApplication


class G9EmploymentAdditionalInfoSpec extends Specification with Tags{

  val employmentAdditionalInfoInput = Seq("empAdditionalInfo.answer" -> "yes", "empAdditionalInfo.text" -> "I do not have much info")
  val employmentAdditionalInfoInputNoText = Seq("empAdditionalInfo.answer" -> "yes")
  val employmentAdditionalInfoInputNo = Seq("empAdditionalInfo.answer" -> "no")

  "Employment " should {

    "present Additional information" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G9EmploymentAdditionalInfo.present(request)
      status(result) mustEqual OK
    }

    "not submit when no input" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G9EmploymentAdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "not submit when answered yes and no text" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(employmentAdditionalInfoInputNoText : _*)

      val result = G9EmploymentAdditionalInfo.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "submit with valid input when answered yes" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(employmentAdditionalInfoInput: _*)

      val result = G9EmploymentAdditionalInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit with valid input when answered no" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(employmentAdditionalInfoInputNo: _*)

      val result = G9EmploymentAdditionalInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }

}
