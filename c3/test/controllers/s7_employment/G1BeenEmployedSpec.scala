package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class G1BeenEmployedSpec extends Specification with Tags {
  "Been Employed" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G1BeenEmployed.present(request)
      status(result) mustEqual OK
    }

    "submit no data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G1BeenEmployed.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "submit been employed" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "yes")
      val result = G1BeenEmployed.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit not been employed" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "no")
      val result = G1BeenEmployed.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "submit with bad data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("beenEmployed" -> "asdf")
      val result = G1BeenEmployed.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section "unit"
}