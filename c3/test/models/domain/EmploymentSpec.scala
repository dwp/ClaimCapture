package models.domain

import controllers.mappings.Mappings
import models.view.CachedClaim
import org.specs2.mock.mockito
import utils.WithApplication
import scala.language.reflectiveCalls
import org.specs2.mutable._
import controllers.Iteration.{Identifier => IterationID}
import mockito.MockitoStubs

class EmploymentSpec extends Specification {

  "Employment" should {

    def claimEmployed = Claim(CachedClaim.key, List(
      Section(SelfEmployment, List(Employment(Mappings.no, Mappings.yes)))
    ))
    def claimNotEmployed = Claim(CachedClaim.key, List(
      Section(SelfEmployment, List(Employment(Mappings.no, Mappings.no)))
    ))
    def claimWithNoEmploymentDetails = Claim(CachedClaim.key, List(
      Section(SelfEmployment, List())
    ))

    "tell you whether the Claim object contains employment" in new WithApplication {
      Employed.isEmployed(claimEmployed) mustEqual true
      Employed.isEmployed(claimNotEmployed) mustEqual false
      Employed.isEmployed(claimWithNoEmploymentDetails) mustEqual false
    }

  }

  "Job" should {
    "add 2 new question groups" in new WithApplication {
      val claiming = new Claiming(){}
      val job = Iteration("1")

      val questionGroup1 = claiming.mockJobQuestionGroup("1")
      val questionGroup2 = claiming.mockJobQuestionGroup("2")

      val updatedJob = job.update(questionGroup1).update(questionGroup2)
      updatedJob.questionGroups.size shouldEqual 2
    }

    "add a question group and update it" in new WithApplication {
      val claiming = new Claiming(){}
      val job = Iteration("1")

      val questionGroup = claiming.mockJobQuestionGroup("1")

      val updatedJob = job.update(questionGroup).update(questionGroup)
      updatedJob.questionGroups.size shouldEqual 1
    }
  }

  "Jobs" should {
    "add 2 new jobs" in new WithApplication {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Iteration("1")).update(Iteration("2"))
      updatedJobs.size shouldEqual 2
    }

    "add a job and update it" in new WithApplication {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Iteration("1")).update(Iteration("1"))
      updatedJobs.size shouldEqual 1
    }

  }

  "Claim" should {
    "iterate over 2 jobs" in new WithApplication {
      val jobs = Jobs().update(Iteration("1")).update(Iteration("2"))
      val claim = Claim(CachedClaim.key).update(jobs)

      claim.questionGroup(Jobs) must beLike { case Some(js: Jobs) => js.size shouldEqual 2 }
    }

    """find first question group i.e. "JobDetails" in a job""" in new WithApplication {
      val claiming = new Claiming(){}
      val jobDetails = claiming.mockQuestionGroup[JobDetails](JobDetails)
      //jobDetails.iterationID returns "2"
      //jobDetails.employerName returns "Toys r not us"

      val jobs = Jobs().update(Iteration("1")).update(Iteration("2").update(jobDetails))
      val claim = Claim(CachedClaim.key).update(jobs)

      claim.questionGroup(Jobs).collect {
        case js: Jobs => js.jobs.find(_.iterationID == "2").collect {
          case j: Iteration => j.questionGroups.find(_.isInstanceOf[JobDetails])
        }
      }.flatten.flatten must beLike { case Some(jd: JobDetails with IterationID) => jd.employerName shouldEqual "Toys r not us" }
    }
  }
}
