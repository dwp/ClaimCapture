package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import yesNo.{YesNoWithDate, YesNoWithDropDownAndText, YesNoWithDropDown}
import play.api.mvc.Call

case class CareYouProvide(theirPersonalDetails: TheirPersonalDetails, theirContactDetails: TheirContactDetails,
                          moreAboutThePerson: MoreAboutThePerson, representatives: RepresentativesForPerson,
                          previousCarerContactDetails: Option[PreviousCarerContactDetails], previousCarerPersonalDetails: Option[PreviousCarerPersonalDetails],
                          moreAboutTheCare: MoreAboutTheCare, oneWhoPays: Option[OneWhoPaysPersonalDetails],
                          contactDetailsPayingPerson: Option[ContactDetailsOfPayingPerson], breaksInCare: BreaksInCare)

case object CareYouProvide extends Section.Identifier {
  val id = "s4"
}

case class TheirPersonalDetails(call: Call,
                                title: String, firstName: String, middleName: Option[String], surname: String,
                                nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                dateOfBirth: DayMonthYear, liveAtSameAddress: String) extends QuestionGroup(TheirPersonalDetails)

case object TheirPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g1"
}

case class TheirContactDetails(call: Call,
                               address: MultiLineAddress, postcode: Option[String], phoneNumber: Option[String] = None) extends QuestionGroup(TheirContactDetails)

case object TheirContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g2"
}

case class MoreAboutThePerson(call: Call,
                              relationship: String, armedForcesPayment: Option[String], claimedAllowanceBefore: String) extends QuestionGroup(MoreAboutThePerson)

case object MoreAboutThePerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g3"
}

case class PreviousCarerPersonalDetails(call: Call,
                                        firstName: Option[String], middleName: Option[String], surname: Option[String],
                                        nationalInsuranceNumber: Option[NationalInsuranceNumber],
                                        dateOfBirth: Option[DayMonthYear]) extends QuestionGroup(PreviousCarerPersonalDetails)

case object PreviousCarerPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g4"
}

case class PreviousCarerContactDetails(call: Call,
                                       address: Option[MultiLineAddress], postcode: Option[String], phoneNumber: Option[String] = None,
                                       mobileNumber: Option[String] = None) extends QuestionGroup(PreviousCarerContactDetails)

case object PreviousCarerContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g5"
}

case class RepresentativesForPerson(call: Call,
                                    youAct: YesNoWithDropDown, someoneElseAct:YesNoWithDropDownAndText) extends QuestionGroup(RepresentativesForPerson)

case object RepresentativesForPerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g6"
}

case class MoreAboutTheCare(call: Call,
                            spent35HoursCaring: String, spent35HoursCaringBeforeClaim:YesNoWithDate, hasSomeonePaidYou: String) extends QuestionGroup(MoreAboutTheCare)

case object MoreAboutTheCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g7"
}

case class OneWhoPaysPersonalDetails(call: Call,
                                     organisation: Option[String] = None, title: Option[String] = None,
                                     firstName: Option[String] = None, middleName: Option[String] = None, surname: Option[String] = None,
                                     amount: Option[String] = None, startDatePayment: Option[DayMonthYear] = None) extends QuestionGroup(OneWhoPaysPersonalDetails)

case object OneWhoPaysPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g8"
}

case class ContactDetailsOfPayingPerson(call: Call,
                                        address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(ContactDetailsOfPayingPerson)

case object ContactDetailsOfPayingPerson extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g9"
}

case class BreaksInCare(call: Call, breaks: List[Break] = Nil) extends QuestionGroup(BreaksInCare) {
  def update(break: Break) = {
    val updated = breaks map {
      b => if (b.id == break.id) break else b
    }

    if (updated.contains(break)) BreaksInCare(call, updated) else BreaksInCare(call, breaks :+ break)
  }

  def delete(breakID: String) = BreaksInCare(call, breaks.filterNot(_.id == breakID))
}

case object BreaksInCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g10"
}

case class Break(id: String, start: DayMonthYear, end: Option[DayMonthYear], whereYou: Whereabouts, wherePerson: Whereabouts, medicalDuringBreak: String)