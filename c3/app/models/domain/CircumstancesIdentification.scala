package models.domain

import models.{DayMonthYear, NationalInsuranceNumber}


case object CircumstancesIdentification extends Section.Identifier {
  val id = "c1"
}

case class CircumstancesReportChange(jsEnabled: Boolean = false,
                                     fullName: String = "",
                                     nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some("")),
                                     dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                     theirFullName: String = "",
                                     theirRelationshipToYou: String = "") extends QuestionGroup(CircumstancesReportChange) {
}

object CircumstancesReportChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g1"
}

