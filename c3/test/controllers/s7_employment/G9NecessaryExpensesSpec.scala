package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.view.CachedClaim

class G9NecessaryExpensesSpec extends Specification with Tags {
  val jobID = "Dummy job ID"

  "Necessary expenses" should {
    "present" in new WithApplication with Claiming {
      val aboutExpenses = AboutExpenses(jobID = jobID, payForAnythingNecessary = "yes")

      val job = Job(jobID).update(aboutExpenses)
      val jobs = Jobs().update(job)

      val claim = Claim().update(jobs)
      Cache.set(claimKey, claim)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val result = G9NecessaryExpenses.present(jobID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G9NecessaryExpenses.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "accept all mandatory data." in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("jobID" -> jobID, "jobTitle" -> "Hacker", "whatAreThose" -> "blah")

      val result = G9NecessaryExpenses.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a (current) job" in new WithApplication with Claiming {
      G3JobDetails.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "jobStartDate.day" -> "1",
        "jobStartDate.month" -> "1",
        "jobStartDate.year" -> "2000",
        "finishedThisJob" -> "no"))

      val result = G9NecessaryExpenses.submit(FakeRequest().withSession(CachedClaim.key -> claimKey)
                                                           .withFormUrlEncodedBody("jobID" -> jobID, "jobTitle" -> "Hacker", "whatAreThose" -> "blah"))

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