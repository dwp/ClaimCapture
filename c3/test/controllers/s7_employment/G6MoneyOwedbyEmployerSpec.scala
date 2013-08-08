package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache
import models.domain.Claim

class G6MoneyOwedbyEmployerSpec extends Specification with Tags {
  val jobID = "Dummy job ID"

  "Money owed by employer" should {
    "present" in new WithApplication with Claiming {
      val additionalWageDetails = mock[AdditionalWageDetails]
      additionalWageDetails.identifier returns AdditionalWageDetails
      additionalWageDetails.jobID returns jobID
      additionalWageDetails.employerOwesYouMoney returns "yes"

      val job = mock[Job]
      job.questionGroups returns additionalWageDetails :: Nil

      val jobs = mock[Jobs]
      jobs.identifier returns Jobs
      jobs.jobs returns job :: Nil
      jobs.questionGroup(jobID, AdditionalWageDetails) returns Some(additionalWageDetails)

      val claim = Claim().update(jobs)
      Cache.set(claimKey, claim)

      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G6MoneyOwedbyEmployer.present(jobID)(request)
      status(result) mustEqual OK
    }

    """require only "job ID".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G6MoneyOwedbyEmployer.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """be added to a (current) job""" in new WithApplication with Claiming {
      G2JobDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes"))

      val result = G6MoneyOwedbyEmployer.submit(FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID))

      status(result) mustEqual SEE_OTHER

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Jobs) must beLike {
        case Some(js: Jobs) => {
          js.size shouldEqual 1

          js.find(_.jobID == jobID) must beLike { case Some(j: Job) => j.questionGroups.size shouldEqual 2 }
        }
      }
    }
  } section("unit",models.domain.Employed.id)
}