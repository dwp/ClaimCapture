package models.domain

import scala.language.reflectiveCalls
import org.specs2.mutable.Specification
import controllers.s7_employment.Employment.jobFormFiller
import models.MultiLineAddress

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

//    "add new question group in existing job" in new Claiming {
//      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
//      val job1 = Job("1").update(jobDetails)
//
//      val jobs = new Jobs(job1 :: Job("2") :: Nil)
//
//      val employerContactDetails = EmployerContactDetails("1", MultiLineAddress(), None, Some("111"))
//      val updatedJob1 = job1.update(employerContactDetails)
//      updatedJob1.questionGroups.size shouldEqual 2
//
//      val updatedJobs = jobs.update(updatedJob1)
//      updatedJobs.size shouldEqual 2
//      updatedJobs.find(_.jobID == "1") must beLike { case Some(Job("1", qgs, false)) => qgs.size shouldEqual 2 }
//    }
//
//    "update existing question group in existing job" in new Claiming {
//      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
//      val employerContactDetails = EmployerContactDetails("1", MultiLineAddress(), None, Some("111"))
//      val job1 = Job("1").update(jobDetails).update(employerContactDetails)
//
//      val jobs = new Jobs(job1 :: Job("2") :: Nil)
//
//      val updatedJob1 = job1.update(EmployerContactDetails("1", MultiLineAddress(), None, Some("222")))
//      updatedJob1.questionGroups.size shouldEqual 2
//
//      val updatedJobs = jobs.update(updatedJob1)
//      updatedJobs.size shouldEqual 2
//
//      updatedJobs.find(_.jobID == "1") must beLike {
//        case Some(Job("1", qgs, false)) => qgs.find(_.isInstanceOf[EmployerContactDetails]) must beLike {
//          case Some(e: EmployerContactDetails) => e.phoneNumber must beSome("222")
//        }
//      }
//    }
//
//    """be directly updated with an existing question group in an existing job""" in new Claiming {
//      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
//      val employerContactDetails = EmployerContactDetails("1", MultiLineAddress(), None, Some("111"))
//      val job1 = Job("1").update(jobDetails).update(employerContactDetails)
//
//      val jobs = new Jobs(job1 :: Job("2") :: Nil)
//
//      val updatedJobs = jobs.update(EmployerContactDetails("1", MultiLineAddress(), None, Some("222")))
//      updatedJobs.size shouldEqual 2
//
//      updatedJobs.find(_.jobID == "1") must beLike {
//        case Some(Job("1", qgs, false)) => qgs.find(_.isInstanceOf[EmployerContactDetails]) must beLike {
//          case Some(e: EmployerContactDetails) => e.phoneNumber must beSome("222")
//        }
//      }
//    }
//
//    "fill existing form" in new Claiming {
//      val jobDetails = mockQuestionGroup[JobDetails](JobDetails)
//      val job1 = Job("1").update(jobDetails)
//
//      val jobs = new Jobs(job1 :: Job("2") :: Nil)
//
//      val employerContactDetails = EmployerContactDetails("1", MultiLineAddress(), None, Some("111"))
//      val updatedJob1 = job1.update(employerContactDetails)
//      updatedJob1.questionGroups.size shouldEqual 2
//
//      val updatedJobs = jobs.update(updatedJob1)
//      updatedJobs.size shouldEqual 2
//      updatedJobs.find(_.jobID == "1") must beLike { case Some(Job("1", qgs, false)) => qgs.size shouldEqual 2 }
//
//      val claim = Claim().update(updatedJobs)
//
//      val form = G4EmployerContactDetails.form.fillWithJobID(EmployerContactDetails, "1")(claim)
//      form.value.isDefined should beTrue
//      val address = new MultiLineAddress()
//      form.value.get must beLike { case EmployerContactDetails(jid,address, None, Some(v)) => jid shouldEqual "1" and(v shouldEqual "111") }
//    }
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
      }.flatten.flatten must beLike { case Some(jd: JobDetails with Iteration.Identifier) => jd.employerName shouldEqual "Toys r not us" }
    }
  }
}