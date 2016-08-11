package models.domain

import models.DayMonthYear
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object Breaks extends Section.Identifier {
  val id = "s7"

  val hospital="hospital"
  val carehome="carehome"
  val none="none"
  val another="another"
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
  val id = s"${Breaks.id}.g1"
}

case class Break(iterationID: String = "",
                 typeOfCare: String = "hospital",
                 whoWasAway: String = "",
                 whenWereYouAdmitted: Option[DayMonthYear] = None,
                 yourStayEnded: Option[YesNoWithDate] = None,
                 whenWasDpAdmitted: Option[DayMonthYear] = None,
                 dpStayEnded: Option[YesNoWithDate] = None,
                 breaksInCareStillCaring: Option[String] = None,
                 yourMedicalProfessional: Option[String] = None,
                 dpMedicalProfessional: Option[String] = None,
                 caringEnded: Option[DayMonthYear] = None,
                 caringStarted: Option[YesNoWithDate] = None,
                 whereWhereYou: Option[RadioWithText] = None,
                 whereWasDp: Option[RadioWithText] = None,
                 caringEndedTime: Option[String] = None,
                 caringStartedTime: Option[String] = None
                )

case class BreaksInCareType(hospital: Option[String] = None,
                            carehome: Option[String] = None,
                            none: Option[String] = None,
                            other: Option[String] = None
                           ) extends QuestionGroup(BreaksInCareType)
case object BreaksInCareType extends QuestionGroup.Identifier {
  val id = s"${Breaks.id}.g2"
}

case class BreaksInCareSummary(breaksummary_other: Option[String] = None,
                               breaksummary_answer: Option[String] = None
                              ) extends QuestionGroup(BreaksInCareSummary)
case object BreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${Breaks.id}.g3"
}

