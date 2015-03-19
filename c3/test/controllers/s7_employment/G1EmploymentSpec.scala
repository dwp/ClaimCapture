package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.view.CachedClaim
import play.api.cache.Cache
import models.domain.{Claim, Iteration, Jobs, JobDetails, Claiming}

class G1EmploymentSpec extends Specification with Tags {
  "Employment" should {
    "get first completed question group for a job" in new WithApplication with Claiming {
      val jobID = "dummyJobID"

      val jobDetails = JobDetails(jobID)
      val job = Iteration(jobID).update(jobDetails)
      val jobs = new Jobs().update(job)

      val claim = Claim(CachedClaim.key).update(jobs)
      Cache.set(claimKey, claim)

      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
      val completedQuestionGroups = Employment.completedQuestionGroups(JobDetails, jobID)(claim)
    }

    "issue an 'error' when deleting a non-existing job" in new WithApplication with Claiming {
      val result = Employment.delete(FakeRequest().withSession(CachedClaim.key -> claimKey).withFormUrlEncodedBody("deleteId" -> "nonExistingJobID"))
      status(result) shouldEqual BAD_REQUEST
    }
  } section("unit", models.domain.Employed.id)
}