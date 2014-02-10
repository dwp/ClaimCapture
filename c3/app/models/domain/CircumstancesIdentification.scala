package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}


case object CircumstancesIdentification extends Section.Identifier {
  val id = "c1"
  //override val expectedMinTimeToCompleteInMillis: Long = 10000
}

case class CircumstancesReportChange(title: String = "",
                    firstName: String = "",
                    middleName: Option[String] = None,
                    lastName: String = "",
                    nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                    dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                     ) extends QuestionGroup(CircumstancesReportChange){
  def otherNames = firstName + middleName.map(" " + _).getOrElse("")
}

object CircumstancesReportChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g1"
}

