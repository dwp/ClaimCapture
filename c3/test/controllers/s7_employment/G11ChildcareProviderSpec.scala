package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain._
import play.api.cache.Cache

class G11ChildcareProviderSpec extends Specification with Tags {
  val jobID = "Dummy job ID"

  "Childcare provider" should {
    "present" in new WithApplication with Claiming {
      val aboutExpenses = mock[AboutExpenses]
      aboutExpenses.identifier returns AboutExpenses
      aboutExpenses.jobID returns jobID
      aboutExpenses.payAnyoneToLookAfterChildren returns "yes"

      val job = mock[Job]
      job.questionGroups returns aboutExpenses :: Nil

      val jobs = mock[Jobs]
      jobs.identifier returns Jobs
      jobs.jobs returns job :: Nil
      jobs.questionGroup(jobID, AboutExpenses) returns Some(aboutExpenses)

      val claim = Claim().update(jobs)
      Cache.set(claimKey, claim)

      val request = FakeRequest().withSession("connected" -> claimKey)
      val result = G11ChildcareProvider.present(jobID)(request)
      status(result) mustEqual OK
    }

    "require all mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("jobID" -> jobID)

      val result = G11ChildcareProvider.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "be added to a (current) job" in new WithApplication with Claiming {
      G2JobDetails.submit(FakeRequest().withSession("connected" -> claimKey)
        withFormUrlEncodedBody(
        "jobID" -> jobID,
        "employerName" -> "Toys r not us",
        "finishedThisJob" -> "yes"))

      val result = G11ChildcareProvider.submit(FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("jobID" -> jobID))

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