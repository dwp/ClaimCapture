package controllers.s_employment

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.{Iteration, Jobs, JobDetails, Claiming}
import models.view.CachedClaim
import utils.WithApplication

class GJobDetailsSpec extends Specification {
  section("unit", models.domain.JobDetails.id)
  "Your job" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = GJobDetails.present("dummyJobID")(request)
      status(result) mustEqual OK
    }

    "miss all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = GJobDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """submit only mandatory data to a "new employment".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(
        "iterationID" -> "1",
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "address.lineTwo" -> "lineTwo",
        "address.lineThree" -> "lineThree",
        "startJobBeforeClaimDate" -> "yes",
        "finishedThisJob" -> "no")

      val result = GJobDetails.submit(request)

      status(result) mustEqual SEE_OTHER

      val claim = getClaimFromCache(result)

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.iterationID == "1") must beLike {
            case Some(j: Iteration) => j.questionGroups.head.asInstanceOf[JobDetails].employerName shouldEqual "Toys r not us"
          }
        }
      }
    }

    """submit all data to a "new employment".""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(
        "iterationID" -> "1",
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "address.lineTwo" -> "lineTwo",
        "address.lineThree" -> "lineThree",
        "startJobBeforeClaimDate" -> "no",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "yes",
        "lastWorkDate.day" -> "1",
        "lastWorkDate.month" -> "1",
        "lastWorkDate.year" -> "2001",
        "p45LeavingDate.day" -> "1",
        "p45LeavingDate.month" -> "1",
        "p45LeavingDate.year" -> "2001",
        "hoursPerWeek" -> "75",
        "payrollEmployeeNumber" -> "445566")

      val result = GJobDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """submit all data to a "new employment" and then delete it.""" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(
        "iterationID" -> "1",
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "address.lineTwo" -> "lineTwo",
        "address.lineThree" -> "lineThree",
        "startJobBeforeClaimDate" -> "no",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "yes",
        "lastWorkDate.day" -> "1",
        "lastWorkDate.month" -> "1",
        "lastWorkDate.year" -> "2001",
        "p45LeavingDate.day" -> "1",
        "p45LeavingDate.month" -> "1",
        "p45LeavingDate.year" -> "2001",
        "hoursPerWeek" -> "75",
        "payrollEmployeeNumber" -> "445566")

      val result = GJobDetails.submit(request)
      status(result) mustEqual SEE_OTHER

      getClaimFromCache(result).questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => js.size shouldEqual 1
      }

      Employment.delete(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result)).withFormUrlEncodedBody("deleteId" -> "1"))

      getClaimFromCache(result).questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => js.size shouldEqual 0
      }
    }
  }
  section("unit", models.domain.JobDetails.id)
}
