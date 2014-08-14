package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim
import models.view.CachedClaim
import app.PensionPaymentFrequency._

class G7PensionSchemesSpec extends Specification with Tags {
  val jobID = "Dummy job ID"
  val howOften_frequency = Other
  val howOften_frequency_other = "Every day and twice on Sundays"

  "Pension schemes - Controller" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G7PensionSchemes.present(jobID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G7PensionSchemes.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID,
                                "payOccupationalPensionScheme" -> "yes", "howMuchPension" -> "100", "howOftenPension.frequency" -> howOften_frequency, "howOftenPension.frequency.other" -> howOften_frequency_other,
                                "payPersonalPensionScheme" -> "yes", "howMuchPersonal" -> "100", "howOftenPersonal.frequency" -> howOften_frequency, "howOftenPersonal.frequency.other" -> howOften_frequency_other)

      val result = G7PensionSchemes.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a (current) job" in new WithApplication with Claiming {
      G3JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = G7PensionSchemes.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID, "payOccupationalPensionScheme" -> "no", "payPersonalPensionScheme" -> "no"))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == jobID) must beLike { case Some(j: Job) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}