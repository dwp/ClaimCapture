package controllers.s_employment

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain._
import models.view.CachedClaim
import utils.WithApplication


class GPensionAndExpensesSpec extends Specification {
  val iterationID = "Dummy job ID"

  "Pension and expenses" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GPensionAndExpenses.present(iterationID)(request)
      println(s"present = $claimKey")
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("iterationID" -> iterationID)

      val result = GPensionAndExpenses.submit(request)
      println(s"require all mandatory data = $claimKey")
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data" in new WithApplication {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "payPensionScheme.answer" -> "yes",
        "payForThings.answer" -> "yes",
        "haveExpensesForJob.answer" -> "yes",
        "payPensionScheme.text" -> "some pension expense",
        "payForThings.text" -> "some expenses to do the job",
        "haveExpensesForJob.text" -> "some job expense"
      )

      val result = GPensionAndExpenses.submit(request)
      println(s"accept all mandatory data = $claimKey")
      status(result) mustEqual SEE_OTHER
    }

    "be added to a current job" in new WithApplication {
      val result1 = GJobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "employerName" -> "Toys r not us",
        "phoneNumber" -> "12345678",
        "address.lineOne" -> "Street Test 1",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = GPensionAndExpenses.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "iterationID" -> iterationID,
        "payPensionScheme.answer" -> "no",
        "payForThings.answer" -> "no",
        "haveExpensesForJob.answer" -> "no"))

      status(result) mustEqual SEE_OTHER

      val mockForm = new MockForm(){}

      val claim = mockForm.getClaimFromCache(result)

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          //TODO: check whether we need to be at qs 2 or 1
          js.find(_.iterationID == iterationID) must beLike { case Some(j: Iteration) => j.questionGroups.size shouldEqual 1 }
        }
      }
    }
  }
  section("unit", models.domain.Employed.id)
}
