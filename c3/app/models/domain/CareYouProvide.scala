package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object CareYouProvide extends Section.Identifier {
  val id = "s5"
}

case class TheirPersonalDetails(relationship: String = "",
                                title: String = "",
                                firstName: String = "",
                                middleName: Option[String] = None,
                                surname: String = "",
                                nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                liveAtSameAddressCareYouProvide: String = "") extends QuestionGroup(TheirPersonalDetails)

case object TheirPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g1"
}

case class TheirContactDetails(address: MultiLineAddress = MultiLineAddress(), postcode: Option[String] = None) extends QuestionGroup(TheirContactDetails)

case object TheirContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g2"
}

case class MoreAboutTheCare(spent35HoursCaring: String = "", spent35HoursCaringBeforeClaim:YesNoWithDate = YesNoWithDate("", None)) extends QuestionGroup(MoreAboutTheCare)

case object MoreAboutTheCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g4"
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
  val id = s"${CareYouProvide.id}.g5"

  def endDateRequired(input: Break): Boolean = input.endTime match {
    case Some(e) => input.end.isDefined
    case _ => true
  }

}

case class Break(iterationID: String = "",
                 start: DayMonthYear = DayMonthYear(None, None, None),
                 startTime:Option[String] = None,
                 end: Option[DayMonthYear] = None,
                 endTime:Option[String] = None,
                 whereYou:RadioWithText = RadioWithText("", None), wherePerson: RadioWithText = RadioWithText("", None),
                 medicalDuringBreak: String = "") extends IterationID

case class BreaksInCareSummary(answer: String = "") extends QuestionGroup(BreaksInCareSummary)

case object BreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g6"
}

