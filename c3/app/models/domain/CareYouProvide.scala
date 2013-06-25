package models.domain

import models.{MultiLineAddress, Whereabouts, NationalInsuranceNumber, DayMonthYear}

object CareYouProvide {
  val id = "s4"
}

case class TheirPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                                nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends QuestionGroup(TheirPersonalDetails.id)

object TheirPersonalDetails {
  val id = s"${CareYouProvide.id}.g1"
}

case class TheirContactDetails(address: MultiLineAddress, postcode: Option[String], phoneNumber: Option[String] = None) extends QuestionGroup(TheirContactDetails.id)

object TheirContactDetails {
  val id = s"${CareYouProvide.id}.g2"
}

case class MoreAboutThePerson(relationship:String, armedForcesPayment:Option[String], claimedAllowanceBefore:String) extends QuestionGroup(MoreAboutThePerson.id)

object MoreAboutThePerson {
  val id = s"${CareYouProvide.id}.g3"
}


case class PreviousCarerPersonalDetails(firstName: Option[String], middleName: Option[String], surname: Option[String],
                                        nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                        dateOfBirth: Option[DayMonthYear]) extends QuestionGroup(TheirPersonalDetails.id)

object PreviousCarerPersonalDetails {
  val id = s"${CareYouProvide.id}.g4"
}

case class RepresentativesForPerson(actForPerson: String,actAs: Option[String],someoneElseActForPerson: String,someoneElseActAs: Option[String], someoneElseFullName: Option[String]) extends QuestionGroup(RepresentativesForPerson.id)

object RepresentativesForPerson {
  val id = s"${CareYouProvide.id}.g6"
}

case class OneWhoPaysPersonalDetails(organisation:Option[String], title:Option[String], firstName:Option[String], middleName:Option[String], surname:Option[String], amount:Option[String], startDatePayment:Option[DayMonthYear]) extends QuestionGroup(OneWhoPaysPersonalDetails.id)

object OneWhoPaysPersonalDetails {
  val id = s"${CareYouProvide.id}.g8"
}

case class HasBreaks(answer: String) extends QuestionGroup(HasBreaks.id)

object HasBreaks {
  val id = s"${CareYouProvide.id}.g10"
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare.id) {
  def update(break: Break) = {
    val updated = breaks map { b => if (b.id == break.id) break else b }

    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(breakID: String) = BreaksInCare(breaks.filterNot(_.id == breakID))
}

object BreaksInCare {
  val id = s"${CareYouProvide.id}.g11"

  def apply() = new BreaksInCare()
}

case class BreakInCare(moreBreaks: String, break: Option[Break])

case class Break(id: String, start: DayMonthYear, end: Option[DayMonthYear], whereYou: Whereabouts, wherePerson: Whereabouts, medicalDuringBreak: Option[String])