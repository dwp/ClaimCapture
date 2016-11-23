package models.domain

import app.ConfigProperties._
import models.DayMonthYear
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

object Breaks extends Identifier(id = "s7") {
  val hospital = "hospital"
  val carehome = "carehome"
  val none = "none"
  val another = "another"

  def maximumBreaks=getIntProperty("maximumBreaksInCare")
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare) {
  def update(break: Break) = {
    val updated = breaks map { b => if (b.iterationID == break.iterationID) break else b }
    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(iterationID: String) = BreaksInCare(breaks.filterNot(_.iterationID == iterationID))

  def hasBreaks = breaks.nonEmpty

  def hasBreaksForType(breakTypeOfCare: String) = {
    var found = false
    for ((break) <- breaks if !found) {
      if (break.typeOfCare == breakTypeOfCare) {
        found = true
      }
    }
    found
  }

  def maximumReached = {
    if (breaks.size >= Breaks.maximumBreaks)
      true
    else
      false
  }
}

object BreaksInCare extends QGIdentifier(id = s"${Breaks.id}.g1")

case class Break(iterationID: String = "",
                 typeOfCare: String = Breaks.hospital,
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
                 whereWasDp: Option[RadioWithText] = None,
                 whereWereYou: Option[RadioWithText] = None,
                 caringEndedTime: Option[String] = None,
                 caringStartedTime: Option[String] = None
                  )

case class BreaksInCareType(hospital: Option[String] = None,
                            carehome: Option[String] = None,
                            none: Option[String] = None,
                            other: Option[String] = None
                             ) extends QuestionGroup(BreaksInCareType)

object BreaksInCareType extends QGIdentifier(id = s"${Breaks.id}.g2")

case class BreaksInCareSummary(breaksummary_other: Option[String] = None,
                               breaksummary_answer: Option[String] = None
                                ) extends QuestionGroup(BreaksInCareSummary)

object BreaksInCareSummary extends QGIdentifier(id = s"${Breaks.id}.g3")


