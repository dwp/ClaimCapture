package models.domain

import models.{MultiLineAddress, DayMonthYear}
import play.api.mvc.Call

object Employed extends Section.Identifier {
  val id = "s7"
}

case class BeenEmployed(beenEmployed: String, call: Call) extends QuestionGroup(BeenEmployed)

object BeenEmployed extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g1"
}

case class Jobs(jobs: List[Job] = Nil) extends QuestionGroup(Jobs) with NoRouting with Iterable[Job] {
  def update(job: Job): Jobs = {
    val updated = jobs map { j => if (j.jobID == job.jobID) job else j }

    if (updated.contains(job)) Jobs(updated) else Jobs(jobs :+ job)
  }

  def update(questionGroup: QuestionGroup with Job.Identifier): Jobs = {
    jobs.find(_.jobID == questionGroup.jobID) match {
      case Some(job: Job) => update(job.update(questionGroup))
      case _ => update(Job(questionGroup.jobID, questionGroup :: Nil))
    }
  }

  override def iterator: Iterator[Job] = jobs.iterator
}

object Jobs extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g99"
}

case class Job(jobID: String, questionGroups: List[QuestionGroup] = Nil) extends Job.Identifier {
  def employerName: String = "DUMMY employer name"

  def title: String = "DUMMY title"

  def update(questionGroup: QuestionGroup): Job = {
    val updated = questionGroups map { qg => if (qg.identifier == questionGroup.identifier) questionGroup else qg }

    if (updated.contains(questionGroup)) copy(questionGroups = updated) else copy(questionGroups = questionGroups :+ questionGroup)
  }
}

object Job {
  trait Identifier {
    val jobID: String

    override def equals(other: Any) = {
      other match {
        case that: Identifier => jobID == that.jobID
        case _ => false
      }
    }

    override def hashCode() = {
      val prime = 41
      prime + jobID.hashCode
    }
  }
}

case class JobDetails(jobID: String,
                      employerName: String, jobStartDate: Option[DayMonthYear], finishedThisJob: String, lastWorkDate:Option[DayMonthYear],
                      p45LeavingDate: Option[DayMonthYear], hoursPerWeek: Option[String],
                      jobTitle: Option[String], payrollEmployeeNumber: Option[String],
                      call: Call) extends QuestionGroup(JobDetails) with Job.Identifier

object JobDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g2"
}

case class EmployerContactDetails(jobID: String,
                                  address: Option[MultiLineAddress], postcode: Option[String], phoneNumber: Option[String],
                                  call: Call) extends QuestionGroup(EmployerContactDetails) with Job.Identifier

object EmployerContactDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g3"
}

case class LastWage(jobID: String,
                    lastPaidDate: Option[DayMonthYear], periodFrom: Option[DayMonthYear], periodTo: Option[DayMonthYear],
                    grossPay: Option[String], payInclusions: Option[String], sameAmountEachTime: Option[String],
                    call: Call) extends QuestionGroup(LastWage) with Job.Identifier

object LastWage extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g4"
}