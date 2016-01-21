package controllers.s_employment

import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.view.CachedClaim
import models.domain.{Claim, Iteration, Jobs, JobDetails, Claiming}
import utils.WithApplication

class GEmploymentSpec extends Specification {
  section("unit", models.domain.Employed.id)
  "Employment" should {
    "get first completed question group for a job" in new WithApplication with Claiming {
      val jobID = "dummyJobID"

      val jobDetails = JobDetails(jobID)
      val job = Iteration(jobID).update(jobDetails)
      val jobs = new Jobs().update(job)

      val claim = Claim(CachedClaim.key).update(jobs)
      cache.set("default"+claimKey, claim)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val completedQuestionGroups = Employment.completedQuestionGroups(JobDetails, jobID)(claim)
    }

    "issue an 'error' when deleting a non-existing job" in new WithApplication with Claiming {
      val result = Employment.delete(FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("deleteId" -> "nonExistingJobID"))
      status(result) shouldEqual BAD_REQUEST
    }
  }
  section("unit", models.domain.Employed.id)
}
