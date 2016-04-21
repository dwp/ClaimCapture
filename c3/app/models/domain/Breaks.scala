package models.domain

import models.DayMonthYear
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object Breaks extends Section.Identifier {
  val id = "s6"
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare) {
  def update(break: Break) = {
    val updated = breaks map { b => if (b.iterationID == break.iterationID) break else b }

    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(iterationID: String) = BreaksInCare(breaks.filterNot(_.iterationID == iterationID))

  def hasBreaks = breaks.nonEmpty
}

case object BreaksInCare extends QuestionGroup.Identifier {
  val id = s"${Breaks.id}.g5"
}

case class Break(iterationID: String = "",
                 start: DayMonthYear = DayMonthYear(None, None, None),
                 startTime:Option[String] = None,
                 wherePerson: RadioWithText = RadioWithText("", None),
                 whereYou:RadioWithText = RadioWithText("", None),
                 hasBreakEnded:YesNoWithDate = YesNoWithDate("", None),
                 endTime:Option[String] = None,
                 medicalDuringBreak: String = "") extends IterationID

case class BreaksInCareSummary(answer: String = "") extends QuestionGroup(BreaksInCareSummary)

case object BreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${Breaks.id}.g6"
}
