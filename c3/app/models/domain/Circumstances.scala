package models.domain

import models.{DayMonthYear, NationalInsuranceNumber}


case object Circumstances extends Section.Identifier {
  val id = "s1"
}

case class CircumstancesAboutYou(title: String = "",
                    firstName: String = "",
                    middleName: Option[String] = None,
                    lastName: String = "",
                    nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                    dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                     ) extends QuestionGroup(CircumstancesAboutYou)

object CircumstancesAboutYou extends QuestionGroup.Identifier {
  val id = s"${Circumstances.id}.g1"
}
