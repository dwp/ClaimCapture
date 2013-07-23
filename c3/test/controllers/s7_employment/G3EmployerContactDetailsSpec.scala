package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import scala.Some

class G3EmployerContactDetailsSpec extends Specification with Tags {
  "Employer's contact details" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFlash("jobID" -> "")
      val result = G3EmployerContactDetails.present(request)
      status(result) mustEqual OK
    }

    """require only "job ID".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1")

      val result = G3EmployerContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      G2JobDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes"))

      val result = G3EmployerContactDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1"))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == "1") must beLike {
            case Some(j: Job) => j.questionGroups.size shouldEqual 2
          }
        }
      }
    }
  } section "unit"
}