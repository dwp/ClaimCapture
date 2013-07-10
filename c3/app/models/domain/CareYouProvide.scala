package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import yesNo.{YesNoWithDropDownAndText, YesNoWithDropDown}

case class CareYouProvide(theirPersonalDetails: TheirPersonalDetails, theirContactDetails: TheirContactDetails,
                          moreAboutThePerson: MoreAboutThePerson, representatives: RepresentativesForPerson,
                          previousCarerContactDetails: Option[PreviousCarerContactDetails], previousCarerPersonalDetails: Option[PreviousCarerPersonalDetails],
                          moreAboutTheCare: MoreAboutTheCare, oneWhoPays: Option[OneWhoPaysPersonalDetails],
                          contactDetailsPayingPerson: Option[ContactDetailsOfPayingPerson], breaksInCare: BreaksInCare)

case object CareYouProvide {
  val id = "s4"
}

case class TheirPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                                nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends QuestionGroup(TheirPersonalDetails.id)

case object TheirPersonalDetails extends QuestionGroup(s"${CareYouProvide.id}.g1")

case class TheirContactDetails(address: MultiLineAddress, postcode: Option[String], phoneNumber: Option[String] = None) extends QuestionGroup(TheirContactDetails.id)

case object TheirContactDetails extends QuestionGroup(s"${CareYouProvide.id}.g2")

case class MoreAboutThePerson(relationship: String, armedForcesPayment: Option[String], claimedAllowanceBefore: String) extends QuestionGroup(MoreAboutThePerson.id)

case object MoreAboutThePerson extends QuestionGroup(s"${CareYouProvide.id}.g3")

case class PreviousCarerPersonalDetails(firstName: Option[String], middleName: Option[String], surname: Option[String],
                                        nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                        dateOfBirth: Option[DayMonthYear]) extends QuestionGroup(PreviousCarerPersonalDetails.id)

case object PreviousCarerPersonalDetails extends QuestionGroup(s"${CareYouProvide.id}.g4")

case class PreviousCarerContactDetails(address: Option[MultiLineAddress], postcode: Option[String], phoneNumber: Option[String] = None,
                                       mobileNumber: Option[String] = None) extends QuestionGroup(PreviousCarerContactDetails.id)

case object PreviousCarerContactDetails extends QuestionGroup(s"${CareYouProvide.id}.g5")

case class RepresentativesForPerson(youAct: YesNoWithDropDown, someoneElseAct:YesNoWithDropDownAndText) extends QuestionGroup(RepresentativesForPerson.id)

case object RepresentativesForPerson extends QuestionGroup(s"${CareYouProvide.id}.g6")

case class MoreAboutTheCare(spent35HoursCaring: String, spent35HoursCaringBeforeClaim: String,
                            careStartDate: Option[DayMonthYear], hasSomeonePaidYou: String) extends QuestionGroup(MoreAboutTheCare.id)

case object MoreAboutTheCare extends QuestionGroup(s"${CareYouProvide.id}.g7")

case class OneWhoPaysPersonalDetails(organisation: Option[String] = None, title: Option[String] = None,
                                     firstName: Option[String] = None, middleName: Option[String] = None, surname: Option[String] = None,
                                     amount: Option[String] = None, startDatePayment: Option[DayMonthYear] = None) extends QuestionGroup(OneWhoPaysPersonalDetails.id)

case object OneWhoPaysPersonalDetails extends QuestionGroup(s"${CareYouProvide.id}.g8")

case class ContactDetailsOfPayingPerson(address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(ContactDetailsOfPayingPerson.id)

case object ContactDetailsOfPayingPerson extends QuestionGroup(s"${CareYouProvide.id}.g9")

case class BreaksInCare(breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare.id) {
  def update(break: Break) = {
    val updated = breaks map {
      b => if (b.id == break.id) break else b
    }

    if (updated.contains(break)) BreaksInCare(updated) else BreaksInCare(breaks :+ break)
  }

  def delete(breakID: String) = BreaksInCare(breaks.filterNot(_.id == breakID))
}

case object BreaksInCare extends QuestionGroup(s"${CareYouProvide.id}.g10") {
  def apply() = new BreaksInCare()
}

case class Break(id: String, start: DayMonthYear, end: Option[DayMonthYear], whereYou: Whereabouts, wherePerson: Whereabouts, medicalDuringBreak: String)