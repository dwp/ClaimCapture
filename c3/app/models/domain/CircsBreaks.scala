package models.domain

import app.ConfigProperties._
import models.DayMonthYear
import models.yesNo.{YesNoDontKnowWithDates, RadioWithText, YesNoWithDate}

object CircsBreaks extends Identifier(id = "c2") {
  val hospital = "hospital"
  val carehome = "carehome"
  val none = "none"
  val another = "another"

  def maximumBreaks=getIntProperty("maximumBreaksInCare")
}

case class CircsBreaksInCare(breaks: List[CircsBreak] = Nil) extends QuestionGroup(CircsBreaksInCare) {
  def update(break: CircsBreak) = {
    val updated = breaks map { b => if (b.iterationID == break.iterationID) break else b }
    if (updated.contains(break)) CircsBreaksInCare(updated) else CircsBreaksInCare(breaks :+ break)
  }

  def delete(iterationID: String) = CircsBreaksInCare(breaks.filterNot(_.iterationID == iterationID))

  def hasCircsBreaks = breaks.nonEmpty

  def hasCircsBreaksForType(breakTypeOfCare: String) = {
    var found = false
    for ((break) <- breaks if !found) {
      if (break.typeOfCare == breakTypeOfCare) {
        found = true
      }
    }
    found
  }

  def circsMaximumReached = {
    if (breaks.size >= CircsBreaks.maximumBreaks)
      true
    else
      false
  }
}

object CircsBreaksInCare extends QGIdentifier(id = s"${CircsBreaks.id}.g14")

case class CircsBreak(iterationID: String = "",
                 typeOfCare: String = CircsBreaks.hospital,
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
                 expectToCareAgain: Option[YesNoDontKnowWithDates]=None,
                 expectToCareAgain2: Option[YesNoDontKnowWithDates]=None,
                 whereWasDp: Option[RadioWithText] = None,
                 whereWereYou: Option[RadioWithText] = None,
                 caringEndedTime: Option[String] = None,
                 caringStartedTime: Option[String] = None)

case class CircsBreaksInCareType(hospital: Option[String] = None,
                            carehome: Option[String] = None,
                            none: Option[String] = None,
                            other: Option[String] = None,
                                 breaksmoreabout: Option[String] = None
                                  ) extends QuestionGroup(CircsBreaksInCareType)

object CircsBreaksInCareType extends QGIdentifier(id = s"${CircsBreaks.id}.g15")

case class CircsBreaksInCareSummary(breaksummary_other: Option[String] = None,
                               breaksummary_answer: Option[String] = None
                                ) extends QuestionGroup(CircsBreaksInCareSummary)

object CircsBreaksInCareSummary extends QGIdentifier(id = s"${CircsBreaks.id}.g16")


