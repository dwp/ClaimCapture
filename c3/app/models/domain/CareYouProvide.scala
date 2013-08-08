package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import yesNo.{YesNoWithDate, YesNoWithDropDownAndText, YesNoWithDropDown}

case class CareYouProvide(theirPersonalDetails: TheirPersonalDetails, theirContactDetails: TheirContactDetails,
                          moreAboutThePerson: MoreAboutThePerson, representatives: RepresentativesForPerson,
                          previousCarerContactDetails: Option[PreviousCarerContactDetails], previousCarerPersonalDetails: Option[PreviousCarerPersonalDetails],
                          moreAboutTheCare: MoreAboutTheCare, oneWhoPays: Option[OneWhoPaysPersonalDetails],
                          contactDetailsPayingPerson: Option[ContactDetailsOfPayingPerson], breaksInCare: BreaksInCare)

case object CareYouProvide extends Section.Identifier {
  val id = "s4"
}

case class TheirPersonalDetails(title: String = "",
                                firstName: String = "",
                                middleName: Option[String] = None,
                                surname: String = "",
                                nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                liveAtSameAddressCareYouProvide: String = "") extends QuestionGroup(TheirPersonalDetails) with NoRouting

case object TheirPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g1"
}

case class TheirContactDetails(address: MultiLineAddress = MultiLineAddress(), postcode: Option[String] = None, phoneNumber: Option[String] = None) extends QuestionGroup(TheirContactDetails) with NoRouting

case object TheirContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g2"
}

case class MoreAboutThePerson(relationship: String = "", armedForcesPayment: Option[String] = None, claimedAllowanceBefore: String = "") extends QuestionGroup(MoreAboutThePerson) with NoRouting

case object MoreAboutThePerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g3"
}

case class PreviousCarerPersonalDetails(firstName: Option[String] = None, middleName: Option[String] = None, surname: Option[String] = None,
                                        nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                        dateOfBirth: Option[DayMonthYear] = None) extends QuestionGroup(PreviousCarerPersonalDetails) with NoRouting

case object PreviousCarerPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g4"
}

case class PreviousCarerContactDetails(address: Option[MultiLineAddress] = None, postcode: Option[String] = None, phoneNumber: Option[String] = None,
                                       mobileNumber: Option[String] = None) extends QuestionGroup(PreviousCarerContactDetails) with NoRouting

case object PreviousCarerContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g5"
}

case class RepresentativesForPerson(youAct: YesNoWithDropDown = YesNoWithDropDown("", None), someoneElseAct:YesNoWithDropDownAndText = YesNoWithDropDownAndText(None, None, None)) extends QuestionGroup(RepresentativesForPerson) with NoRouting

case object RepresentativesForPerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g6"

  def validate(input: RepresentativesForPerson): Boolean = input.youAct.answer match {
    case "yes" => true
    case "no" => input.someoneElseAct.answer.isDefined
    case _ => false
  }
}

case class MoreAboutTheCare(spent35HoursCaring: String = "", spent35HoursCaringBeforeClaim:YesNoWithDate = YesNoWithDate("", None), hasSomeonePaidYou: String = "") extends QuestionGroup(MoreAboutTheCare) with NoRouting

case object MoreAboutTheCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g7"
}

case class OneWhoPaysPersonalDetails(organisation: Option[String] = None, title: Option[String] = None,
                                     firstName: Option[String] = None, middleName: Option[String] = None, surname: Option[String] = None,
                                     amount: Option[String] = None, startDatePayment: Option[DayMonthYear] = None) extends QuestionGroup(OneWhoPaysPersonalDetails) with NoRouting

case object OneWhoPaysPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g8"
}

case class ContactDetailsOfPayingPerson(address: Option[MultiLineAddress] = None, postcode: Option[String] = None) extends QuestionGroup(ContactDetailsOfPayingPerson) with NoRouting

case object ContactDetailsOfPayingPerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g9"
}

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare) with NoRouting {
  def update(break: Break) = {
    val updated = breaks map { b => if (b.id == break.id) break else b }

    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(breakID: String) = BreaksInCare(breaks.filterNot(_.id == breakID))

  def hasBreaks = !breaks.isEmpty
}

case object BreaksInCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g10"
}

case class Break(id: String, start: DayMonthYear, end: Option[DayMonthYear], whereYou: Whereabouts, wherePerson: Whereabouts, medicalDuringBreak: String)