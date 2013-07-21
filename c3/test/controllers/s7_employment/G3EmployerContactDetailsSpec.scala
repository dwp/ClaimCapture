package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G3EmployerContactDetailsSpec extends Specification with Tags {
  "Employer's contact details" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G3EmployerContactDetails.present(request)
      status(result) mustEqual OK
    }

    """require only "job ID".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("jobID" -> "1")
      val result = G3EmployerContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}