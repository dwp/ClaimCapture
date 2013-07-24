package models.domain

import models.DayMonthYear
import controllers.Mappings._
import play.api.mvc.Call
import models.PaymentFrequency
import models.MultiLineAddress
import models.PeriodFromTo
import scala.Some

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

  def delete(questionGroup: QuestionGroup with Job.Identifier): Jobs = {
    jobs.find(_.jobID == questionGroup.jobID) match {
      case Some(job: Job) => update(Job(questionGroup.jobID,job.questionGroups.filterNot(_.identifier == questionGroup.identifier)))
      case _ => this
    }
  }

  def apply(jobID:String): Option[Job] = jobs.find(_.jobID == jobID)

  def questionGroup(questionGroup: QuestionGroup with Job.Identifier):Option[QuestionGroup] = {
    this(questionGroup.jobID).getOrElse(None) match {
      case Some(v:Job) => v(questionGroup)
      case None => None
    }

  }



  override def iterator: Iterator[Job] = jobs.iterator
}

object Jobs extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g99"
}

case class Job(jobID: String, questionGroups: List[QuestionGroup with Job.Identifier] = Nil) extends Job.Identifier with Iterable[QuestionGroup with Job.Identifier] {
  def employerName = jobDetails(_.employerName)

  def title = jobDetails(_.jobTitle.getOrElse(""))

  def update(questionGroup: QuestionGroup with Job.Identifier): Job = {
    val updated = questionGroups map { qg => if (qg.identifier == questionGroup.identifier) questionGroup else qg }
    if (updated.contains(questionGroup)) copy(questionGroups = updated) else copy(questionGroups = questionGroups :+ questionGroup)
  }

  private def jobDetails(f: JobDetails => String) = questionGroups.find(_.isInstanceOf[JobDetails]) match {
    case Some(j: JobDetails) => f(j)
    case _ => ""
  }

  def apply(questionGroup: QuestionGroup ): Option[QuestionGroup] = questionGroups.find(_.identifier == questionGroup.identifier)

  override def iterator: Iterator[QuestionGroup with Job.Identifier] = questionGroups.iterator
}

object Job {
  trait Identifier {
    val jobID: String
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
                    lastPaidDate: Option[DayMonthYear], period: Option[PeriodFromTo],
                    grossPay: Option[String], payInclusions: Option[String], sameAmountEachTime: Option[String],
                    call: Call) extends QuestionGroup(LastWage) with Job.Identifier

object LastWage extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g4"
}

case class AdditionalWageDetails(jobID:String,
                                 oftenGetPaid: Option[PaymentFrequency],whenGetPaid:Option[String],
                                 holidaySickPay: Option[String], anyOtherMoney: String, otherMoney:Option[String],
                                 employeeOwesYouMoney:String,
                                 call: Call)extends QuestionGroup(AdditionalWageDetails) with Job.Identifier

object AdditionalWageDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g5"

  def validateOtherMoney(input: AdditionalWageDetails): Boolean = input.anyOtherMoney match {
    case `yes` => input.otherMoney.isDefined
    case `no` => true
  }

  def validateOftenGetPaid(input: AdditionalWageDetails): Boolean = input.oftenGetPaid match {
    case Some(pf) => pf.other.isDefined
    case None => true
  }
}

case class MoneyOwedbyEmployer(jobID: String,
                               howMuch: Option[String], owedPeriod: Option[PeriodFromTo], owedFor: Option[String],
                               shouldBeenPaidBy: Option[DayMonthYear], whenWillGetIt: Option[String],
                               call: Call) extends QuestionGroup(MoneyOwedbyEmployer) with Job.Identifier

object MoneyOwedbyEmployer extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g6"
}

case class PensionSchemes(jobID: String,
                          payOccupationalPensionScheme: String, howMuchPension: Option[String], howOftenPension:Option[String],
                          payPersonalPensionScheme: String, howMuchPersonal: Option[String], howOftenPersonal: Option[String],
                          call: Call) extends QuestionGroup(PensionSchemes) with Job.Identifier

object PensionSchemes extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g7"
}

case class AboutExpenses(jobID: String,
                          payForAnythingNecessary: String, payAnyoneToLookAfterChildren: String, payAnyoneToLookAfterPerson: String,
                          call: Call) extends QuestionGroup(AboutExpenses) with Job.Identifier

object AboutExpenses extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g8"
}