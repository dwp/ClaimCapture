package models.domain

import models.DayMonthYear
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object OldBreaks extends Section.Identifier {
  val id = "s7"
}

case class OldBreaksInCare(breaks: List[OldBreak] = Nil) extends QuestionGroup(OldBreaksInCare) {
  def update(break: OldBreak) = {
    val updated = breaks map { b => if (b.iterationID == break.iterationID) break else b }

    if (updated.contains(break)) OldBreaksInCare(updated) else OldBreaksInCare(breaks :+ break)
  }

  def delete(iterationID: String) = OldBreaksInCare(breaks.filterNot(_.iterationID == iterationID))

  def hasBreaks = breaks.nonEmpty
}

case object OldBreaksInCare extends QuestionGroup.Identifier {
  val id = s"${OldBreaks.id}.g5"
}

case class OldBreak(iterationID: String = "",
                 start: DayMonthYear = DayMonthYear(None, None, None),
                 startTime:Option[String] = None,
                 wherePerson: RadioWithText = RadioWithText("", None),
                 whereYou:RadioWithText = RadioWithText("", None),
                 hasBreakEnded:YesNoWithDate = YesNoWithDate("", None),
                 endTime:Option[String] = None,
                 medicalDuringBreak: String = "") extends IterationID

case class OldBreaksInCareSummary(answer: String = "") extends QuestionGroup(OldBreaksInCareSummary)

case object OldBreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${OldBreaks.id}.g6"
}
