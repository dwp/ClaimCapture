package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming

class EmploymentSpec extends Specification with Tags {
  "Employment" should {
    "present completion" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = Employment.completed(request)
      status(result) mustEqual OK
    }

    """progress to "self employment".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = Employment.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}