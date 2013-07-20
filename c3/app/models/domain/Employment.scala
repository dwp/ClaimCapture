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

case class Jobs(jobs: List[Job] = Nil) extends QuestionGroup(Job) with NoRouting with Iterable[Job] {
  def update(job: Job) = {
    val updated = jobs map { j => if (j.id == job.id) job else j }

    if (updated.contains(job)) Jobs(updated) else Jobs(jobs :+ job)
  }

  override def iterator: Iterator[Job] = jobs.iterator
}

object Jobs extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g1a"
}

case class Job(id: String, questionGroups: List[QuestionGroup] = Nil) extends QuestionGroup(Job) with NoRouting {
  def employerName: String = "DUMMY employer name"

  def title: String = "DUMMY title"

  def update(questionGroup: QuestionGroup) = {
    val updated = questionGroups map { qg => if (qg.identifier == questionGroup.identifier) questionGroup else qg }

    if (updated.contains(questionGroup)) copy(questionGroups = updated) else copy(questionGroups = questionGroups :+ questionGroup)
  }
}

object Job extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g1b"
}

case class JobDetails(employerName: String, jobStartDate: Option[DayMonthYear], finishedThisJob: String, lastWorkDate:Option[DayMonthYear],
                      p45LeavingDate: Option[DayMonthYear], hoursPerWeek: Option[String],
                      jobTitle: Option[String], payrollEmployeeNumber: Option[String],
                      call: Call) extends QuestionGroup(JobDetails)

object JobDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g2"
}

case class EmployerContactDetails(address: Option[MultiLineAddress], postcode: Option[String], phoneNumber: Option[String],
                                  call: Call) extends QuestionGroup(EmployerContactDetails)

object EmployerContactDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g3"
}

case class LastWage(lastPaidDate: Option[DayMonthYear], periodFrom: Option[DayMonthYear], periodTo: Option[DayMonthYear],
                    grossPay: Option[String], payInclusions: Option[String], sameAmountEachTime: Option[String],
                    call: Call) extends QuestionGroup(LastWage)

object LastWage extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g4"
}