package models.domain

import org.specs2.mutable.Specification

class EmploymentSpec extends Specification {
  "Jobs" should {
    "add 2 new jobs" in {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Job("1")).update(Job("2"))
      updatedJobs.size shouldEqual 2
    }

    "add a job and update it" in {
      val jobs = Jobs()

      val updatedJobs = jobs.update(Job("1")).update(Job("1"))
      updatedJobs.size shouldEqual 1
    }
  }

  "Job" should {
    "add 2 new question groups" in new Claiming {
      val job = Job("1")

      val questionGroup1 = mockQuestionGroup("1")
      val questionGroup2 = mockQuestionGroup("2")

      val updatedJob = job.update(questionGroup1).update(questionGroup2)
      updatedJob.questionGroups.size shouldEqual 2
    }

    "add a question group and update it" in new Claiming {
      val job = Job("1")

      val questionGroup = mockQuestionGroup("1")

      val updatedJob = job.update(questionGroup).update(questionGroup)
      updatedJob.questionGroups.size shouldEqual 1
    }
  }
}