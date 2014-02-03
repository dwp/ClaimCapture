package models.domain

import models.DayMonthYear

case object CircumstancesPermanentlyStoppedCaring extends Section.Identifier {
  val id = "c1"
}

case class CircumstancesStoppedCaring(
                    stoppedCaringDate: DayMonthYear = DayMonthYear(None, None, None),
                    moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesStoppedCaring)

object CircumstancesStoppedCaring extends QuestionGroup.Identifier {
  val id = s"${CircumstancesPermanentlyStoppedCaring.id}.g1"
}