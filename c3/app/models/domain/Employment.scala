package models.domain

import models.DayMonthYear


object Employed {
  val id = "s8"
}

object BeenEmployed extends QuestionGroup(s"${Employed.id}.g1")
case class BeenEmployed(beenEmployed:String) extends QuestionGroup(BeenEmployed.id)

object JobDetails extends QuestionGroup(s"${Employed.id}.g2")
case class JobDetails(employerName:String,jobStartDate:Option[DayMonthYear],finishedThisJob:String,lastWorkDate:Option[DayMonthYear],
                      p45LeavingDate:Option[DayMonthYear],hoursPerWeek:Option[String],jobTitle:Option[String],payrollEmpNumber:Option[String]) extends QuestionGroup(JobDetails.id)





