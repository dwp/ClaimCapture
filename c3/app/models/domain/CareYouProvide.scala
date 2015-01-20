package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import models.yesNo.{RadioWithText, YesNoWithDate}

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
                                armedForcesPayment: String = "",
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
    val updated = breaks map { b => if (b.id == break.id) break else b }

    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(breakID: String) = BreaksInCare(breaks.filterNot(_.id == breakID))

  def hasBreaks = breaks.nonEmpty
}

case object BreaksInCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g5"
}

case class Break(id: String = "",
                 start: DayMonthYear = DayMonthYear(None, None, None), end: Option[DayMonthYear] = None,
                 whereYou:RadioWithText = RadioWithText("", None), wherePerson: RadioWithText = RadioWithText("", None),
                 medicalDuringBreak: String = "")

case class BreaksInCareSummary(answer: String = "") extends QuestionGroup(BreaksInCareSummary)

case object BreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g6"
}