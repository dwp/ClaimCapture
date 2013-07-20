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

case class JobDetails(employerName: String, jobStartDate: Option[DayMonthYear], finishedThisJob: String, lastWorkDate:Option[DayMonthYear],
                      p45LeavingDate: Option[DayMonthYear], hoursPerWeek: Option[String],
                      jobTitle: Option[String], payrollEmployeeNumber: Option[String],
                      call: Call) extends QuestionGroup(JobDetails)

object JobDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g2"
}

case class EmployerContactDetails(call: Call,
                                  address: Option[MultiLineAddress], postcode: Option[String], phoneNumber: Option[String]) extends QuestionGroup(EmployerContactDetails)

object EmployerContactDetails extends QuestionGroup.Identifier {
  val id = s"${Employed.id}.g3"
}