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
                       maritalStatus: String = "",
                       alwaysLivedUK: String = "") extends QuestionGroup(YourDetails) with NoRouting {
  def otherNames = firstName + (middleName match {
    case Some(m: String) => s" $m"
    case _ => ""
  })
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: MultiLineAddress = MultiLineAddress(None,None,None),
                          postcode: Option[String] = None,
                          phoneNumber: Option[String] = None,
                          mobileNumber: Option[String] = None) extends QuestionGroup(ContactDetails) with NoRouting

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class TimeOutsideUK(livingInUK: LivingInUK = LivingInUK(), visaReference: Option[String] = None) extends QuestionGroup(TimeOutsideUK) with NoRouting

object TimeOutsideUK extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g3"
}

case class ClaimDate(dateOfClaim: DayMonthYear) extends QuestionGroup(ClaimDate) with NoRouting

object ClaimDate extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g4"
}

case class MoreAboutYou(hadPartnerSinceClaimDate: String = "",
                        beenInEducationSinceClaimDate: String = "",
                        receiveStatePension: String = "") extends QuestionGroup(MoreAboutYou) with NoRouting

object MoreAboutYou extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g5"
}

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String = "", beenEmployedSince6MonthsBeforeClaim: String = "") extends QuestionGroup(Employment) with NoRouting

object Employment extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g6"
}

case class PropertyAndRent(ownProperty: String = "", hasSublet: String = "") extends QuestionGroup(PropertyAndRent) with NoRouting

object PropertyAndRent extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g7"
}