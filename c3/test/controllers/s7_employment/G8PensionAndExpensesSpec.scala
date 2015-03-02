package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import models.view.CachedClaim
import scala.Some

class G8PensionAndExpensesSpec extends Specification with Tags {
  val iterationID = "Dummy job ID"

  "Pension and expenses" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G8PensionAndExpenses.present(iterationID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("iterationID" -> iterationID)

      val result = G8PensionAndExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "payPensionScheme.answer" -> "yes",
        "payForThings.answer" -> "yes",
        "haveExpensesForJob.answer" -> "yes",
        "payPensionScheme.text" -> "some pension expense",
        "payForThings.text" -> "some expenses to do the job",
        "haveExpensesForJob.text" -> "some job expense"
      )
      val result = G8PensionAndExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a current job" in new WithApplication with Claiming {
      val result1 = G3JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = G8PensionAndExpenses.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "payPensionScheme.answer" -> "no",
        "payForThings.answer" -> "no",
        "haveExpensesForJob.answer" -> "no"))

      status(result) mustEqual SEE_OTHER

      val claim = getClaimFromCache(result)

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          //TODO: check whether we need to be at qs 2 or 1
          js.find(_.iterationID == iterationID) must beLike { case Some(j: Iteration) => j.questionGroups.size shouldEqual 1 }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}