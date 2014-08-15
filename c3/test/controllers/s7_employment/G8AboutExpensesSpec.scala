package controllers.s7_employment

import app.PensionPaymentFrequency._
import models.domain._
import models.view.CachedClaim
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G8AboutExpensesSpec extends Specification with Tags {
  val jobID = "Dummy job ID"

  "About expenses" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest()
      val result = G8AboutExpenses.present(jobID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G8AboutExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("jobID" -> jobID,
        "haveExpensesForJob" -> "yes", "payAnyoneToLookAfterChildren" -> "yes", "payAnyoneToLookAfterPerson" -> "yes",
        "jobTitle" -> "some title",
        "whatExpensesForJob" -> "some expense",
        "nameLookAfterChildren" -> "Jane Doe",
        "howMuchLookAfterChildren" -> "125.40",
        "howOftenLookAfterChildren.frequency" -> Other,
        "howOftenLookAfterChildren.frequency.other" -> "every day",
        "relationToYouLookAfterChildren" -> "none",
        "relationToPersonLookAfterChildren" -> "some relation",
        "nameLookAfterPerson" -> "John Daney",
        "howMuchLookAfterPerson" -> "123",
        "howOftenLookAfterPerson.frequency" -> Other,
        "howOftenLookAfterPerson.frequency.other" -> "every other day",
        "relationToYouLookAfterPerson" -> "no relation",
        "relationToPersonLookAfterPerson" -> "some other relation"
      )
      val result = G8AboutExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a (current) job" in new WithApplication with Claiming {
      val result1 = G3JobDetails.submit(FakeRequest()
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "address.lineOne" -> "Street Test 1",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result2 = G8AboutExpenses.submit(FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result1))
        withFormUrlEncodedBody("jobID" -> jobID,
        "haveExpensesForJob" -> "no", "payAnyoneToLookAfterChildren" -> "no", "payAnyoneToLookAfterPerson" -> "no"))

      status(result2) mustEqual SEE_OTHER

      val claim = getClaimFromCache(result2)

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == jobID) must beLike { case Some(j: Job) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section("unit", models.domain.Employed.id)
}