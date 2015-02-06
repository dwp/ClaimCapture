package models.domain

import scala.language.reflectiveCalls
import org.specs2.mutable.Specification
import controllers.s7_employment.Employment.jobFormFiller
import models.MultiLineAddress
import controllers.Iteration.{Identifier => IterationID}

class EmploymentSpec extends Specification {
  "Job" should {
    "add 2 new question groups" in new Claiming {
      val job = Iteration("1")

      val questionGroup1 = mockJobQuestionGroup("1")
      val questionGroup2 = mockJobQuestionGroup("2")

      val updatedJob = job.update(questionGroup1).update(questionGroup2)
      updatedJob.questionGroups.size shouldEqual 2
    }

    "add a question group and update it" in new Claiming {
      val job = Iteration("1")

      val questionGroup = mockJobQuestionGroup("1")

      val updatedJob = job.update(questionGroup).update(questionGroup)
      updatedJob.questionGroups.size shouldEqual 1
    }
  }

  "Jobs" should {
    "add 2 new jobs" in {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Iteration("1")).update(Iteration("2"))
      updatedJobs.size shouldEqual 2
    }

    "add a job and update it" in {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Iteration("1")).update(Iteration("1"))
      updatedJobs.size shouldEqual 1
    }

  }

  "Claim" should {
    "iterate over 2 jobs" in {
      val jobs = Jobs().update(Iteration("1")).update(Iteration("2"))
      val claim = Claim().update(jobs)

      claim.questionGroup(Jobs) must beLike { case Some(js: Jobs) => js.size shouldEqual 2 }
    }

    """find first question group i.e. "JobDetails" in a job""" in new Claiming {
      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
      jobDetails.iterationID returns "2"
      jobDetails.employerName returns "Toys r not us"

      val jobs = Jobs().update(Iteration("1")).update(Iteration("2").update(jobDetails))
      val claim = Claim().update(jobs)

      claim.questionGroup(Jobs).collect {
        case js: Jobs => js.jobs.find(_.iterationID == "2").collect {
          case j: Iteration => j.questionGroups.find(_.isInstanceOf[JobDetails])
        }
      }.flatten.flatten must beLike { case Some(jd: JobDetails with IterationID) => jd.employerName shouldEqual "Toys r not us" }
    }
  }
}