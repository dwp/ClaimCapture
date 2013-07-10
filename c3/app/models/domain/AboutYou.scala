package models.domain

import models.{LivingInUK, NationalInsuranceNumber, MultiLineAddress, DayMonthYear}

case class AboutYou(yourDetails: YourDetails,
                    contactDetails: ContactDetails,
                    timeOutsideUK : Option[TimeOutsideUK],
                    claimDate : ClaimDate)

case object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                       otherSurnames: Option[String], nationalInsuranceNumber: Option[NationalInsuranceNumber], nationality: String,
                       dateOfBirth: DayMonthYear, maritalStatus: String, alwaysLivedUK: String) extends QuestionGroup(YourDetails.id)

case object YourDetails extends QuestionGroup(s"${AboutYou.id}.g1")

case class ContactDetails(address: MultiLineAddress, postcode: Option[String], phoneNumber: Option[String], mobileNumber: Option[String]) extends QuestionGroup(ContactDetails.id)

case object ContactDetails extends QuestionGroup(s"${AboutYou.id}.g2")

case class TimeOutsideUK(livingInUK:LivingInUK, visaReference: Option[String]) extends QuestionGroup(TimeOutsideUK.id)

case object TimeOutsideUK extends QuestionGroup(s"${AboutYou.id}.g3")

case class ClaimDate(dateOfClaim: DayMonthYear) extends QuestionGroup(ClaimDate.id)

case object ClaimDate extends QuestionGroup(s"${AboutYou.id}.g4")

case class MoreAboutYou(hadPartnerSinceClaimDate: String, eitherClaimedBenefitSinceClaimDate: String,
                        beenInEducationSinceClaimDate: String, receiveStatePension: String) extends QuestionGroup(MoreAboutYou.id)

case object MoreAboutYou extends QuestionGroup(s"${AboutYou.id}.g5")

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String, beenEmployedSince6MonthsBeforeClaim: String) extends QuestionGroup(Employment.id)

case object Employment extends QuestionGroup(s"${AboutYou.id}.g6")

case class PropertyAndRent(ownProperty: String, hasSublet: String) extends QuestionGroup(PropertyAndRent.id)

case object PropertyAndRent extends QuestionGroup(s"${AboutYou.id}.g7")