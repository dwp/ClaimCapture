package models.domain

import app.XMLValues._
import models.{DayMonthYear, NationalInsuranceNumber}


case object CircumstancesIdentification extends Section.Identifier {
  val id = "c1"
}
case class ReportChanges(jsEnabled: Boolean = false, reportChanges: String = NotAsked) extends QuestionGroup(ReportChanges)

object ReportChanges extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g1"
}
case class CircumstancesReportChange(fullName: String = "",
                                     nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some("")),
                                     dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                     theirFullName: String = "",
                                     theirRelationshipToYou: String = "") extends QuestionGroup(CircumstancesReportChange) {
}

object CircumstancesReportChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g2"
}


