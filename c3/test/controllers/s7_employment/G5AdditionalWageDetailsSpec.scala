package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim

class G5AdditionalWageDetailsSpec extends Specification with Tags {
  "Additional wage details" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFlash("jobID" -> "")
      val result = G5AdditionalWageDetails.present("Dummy job ID")(request)
      status(result) mustEqual OK
    }

    """require "job ID" and "Employee owes you money".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1", "employerOwesYouMoney" -> "no", "anyOtherMoney" -> "no")

      val result = G5AdditionalWageDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      pending("skipped till G4 is done")

      G2JobDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes"))

      G3EmployerContactDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1"))


      //G4 stuff

      val result = G5AdditionalWageDetails.submit(FakeRequest().withSession("connected" -> claimKey)
                    .withFormUrlEncodedBody(
                    "jobID" -> "1",
                    "employeeOwesYouMoney" -> "no"))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == "1") must beLike {
            case Some(j: Job) => j.questionGroups.size shouldEqual 4
          }
        }
      }
    }
  } section("unit",models.domain.Employed.id)
}