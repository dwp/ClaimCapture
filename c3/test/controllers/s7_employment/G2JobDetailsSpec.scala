package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G2JobDetailsSpec extends Specification with Tags {
  "Details about your job" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2JobDetails.present(request)
      status(result) mustEqual OK
    }

    "miss all mandatory data" in new WithApplication with Claiming {

      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2JobDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    




  } section "unit"
}