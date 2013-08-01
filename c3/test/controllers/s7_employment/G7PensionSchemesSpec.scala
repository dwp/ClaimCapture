package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim

class G7PensionSchemesSpec extends Specification with Tags {
  val jobID = "Dummy job ID"

  "Pension schemes" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G7PensionSchemes.present(jobID)(request)
      status(result) mustEqual OK
    }

    """require all mandatory data.""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G7PensionSchemes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept all mandatory data.""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID, "payOccupationalPensionScheme" -> "blah", "payPersonalPensionScheme" -> "blah")

      val result = G7PensionSchemes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      G2JobDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes"))

      val result = G7PensionSchemes.submit(FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID, "payOccupationalPensionScheme" -> "blah", "payPersonalPensionScheme" -> "blah"))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == jobID) must beLike { case Some(j: Job) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section "unit"
}