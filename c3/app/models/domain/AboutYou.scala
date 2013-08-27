package models.domain

import models.{LivingInUK, NationalInsuranceNumber, MultiLineAddress, DayMonthYear}

object AboutYou extends Section.Identifier {
  val id = "s2"
}

case class YourDetails(title: String = "",
                       firstName: String = "",
                       middleName: Option[String] = None,
                       surname: String = "",
                       otherSurnames: Option[String] = None,
                       nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                       nationality: String = "",
                       dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                       alwaysLivedUK: String = "",
                       maritalStatus: String = "") extends QuestionGroup(YourDetails) {

  def otherNames = firstName + middleName.map(" " + _).getOrElse("")
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: MultiLineAddress = MultiLineAddress(None, None, None),
                          postcode: Option[String] = None,
                          phoneNumber: Option[String] = None,
                          contactYouByTextphone: Option[String] = None,
                          mobileNumber: Option[String] = None) extends QuestionGroup(ContactDetails)

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class TimeOutsideUK(livingInUK: LivingInUK = LivingInUK()) extends QuestionGroup(TimeOutsideUK)

object TimeOutsideUK extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g3"
}

case class ClaimDate(dateOfClaim: DayMonthYear = DayMonthYear()) extends QuestionGroup(ClaimDate)

object ClaimDate extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"
}

case class MoreAboutYou(hadPartnerSinceClaimDate: String = "",
                        beenInEducationSinceClaimDate: String = "",
                        receiveStatePension: String = "") extends QuestionGroup(MoreAboutYou)

object MoreAboutYou extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g5"
}

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String = "", beenEmployedSince6MonthsBeforeClaim: String = "") extends QuestionGroup(Employment)

object Employment extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g6"
}