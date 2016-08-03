package models.domain

import models.DayMonthYear
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object Breaks extends Section.Identifier {
  val id = "s7"

  val hospital="hospital"
  val carehome="carehome"
  val none="none"
}

case class BreaksInCare(breaks: List[OldBreak] = Nil) extends QuestionGroup(OldBreaksInCare) {
  def update(break: OldBreak) = {
  }

  def delete(iterationID: String) = {
  }

  def hasBreaks = breaks.nonEmpty
}

case object BreaksInCare extends QuestionGroup.Identifier {
  val id = s"${OldBreaks.id}.g5"
}

case class Break(iterationID: String = "") extends IterationID

case class BreaksInCareType(hospital: Option[String] = None,
                            carehome: Option[String] = None,
                            none: Option[String] = None,
                            other: Option[String] = None
                             ) extends QuestionGroup(BreaksInCareType)

case object BreaksInCareType extends QuestionGroup.Identifier {
  val id = s"${Breaks.id}.g6"
}
