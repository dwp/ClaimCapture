package models.domain

import org.specs2.mutable.Specification

class EmploymentSpec extends Specification {
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

    "add new question group in existing job" in new Claiming {
      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
      val job1 = Job("1").update(jobDetails)

      val jobs = new Jobs(job1 :: Job("2") :: Nil)

      val employerContactDetails = EmployerContactDetails("1", None, None, Some("111"), NoRouting)
      val updatedJob1 = job1.update(employerContactDetails)
      updatedJob1.questionGroups.size shouldEqual 2

      val updatedJobs = jobs.update(updatedJob1)
      updatedJobs.size shouldEqual 2
      updatedJobs.find(_.jobID == "1") must beLike { case Some(Job("1", qgs)) => qgs.size shouldEqual 2 }
    }

    "update existing question group in existing job" in new Claiming {
      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
      val employerContactDetails = EmployerContactDetails("1", None, None, Some("111"), NoRouting)
      val job1 = Job("1").update(jobDetails).update(employerContactDetails)

      val jobs = new Jobs(job1 :: Job("2") :: Nil)

      val updatedJob1 = job1.update(EmployerContactDetails("1", None, None, Some("222"), NoRouting))
      updatedJob1.questionGroups.size shouldEqual 2

      val updatedJobs = jobs.update(updatedJob1)
      updatedJobs.size shouldEqual 2

      updatedJobs.find(_.jobID == "1") must beLike {
        case Some(Job("1", qgs)) => qgs.find(_.isInstanceOf[EmployerContactDetails]) must beLike {
          case Some(e: EmployerContactDetails) => e.phoneNumber must beSome("222")
        }
      }
    }

    """be directly updated with an existing question group in an existing job""" in new Claiming {
      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
      val employerContactDetails = EmployerContactDetails("1", None, None, Some("111"), NoRouting)
      val job1 = Job("1").update(jobDetails).update(employerContactDetails)

      val jobs = new Jobs(job1 :: Job("2") :: Nil)

      val updatedJobs = jobs.update(EmployerContactDetails("1", None, None, Some("222"), NoRouting))
      updatedJobs.size shouldEqual 2

      updatedJobs.find(_.jobID == "1") must beLike {
        case Some(Job("1", qgs)) => qgs.find(_.isInstanceOf[EmployerContactDetails]) must beLike {
          case Some(e: EmployerContactDetails) => e.phoneNumber must beSome("222")
        }
      }
    }
  }
}