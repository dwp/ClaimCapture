package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import models.view.CachedClaim

class G5AdditionalWageDetailsSpec extends Specification with Tags {
  "Additional wage details" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFlash("jobID" -> "")
      val result = G5AdditionalWageDetails.present("Dummy job ID")(request)
      status(result) mustEqual OK
    }

    """require "job ID" and "Employee owes you money".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> "1", "employerOwesYouMoney" -> "no")

      val result = G5AdditionalWageDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {


      G2JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> "1",
        "employerName" -> "Toys r not us",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = G5AdditionalWageDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
                    .withFormUrlEncodedBody("jobID" -> "1", "employerOwesYouMoney" -> "no"))

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
  } section("unit", models.domain.Employed.id)
}