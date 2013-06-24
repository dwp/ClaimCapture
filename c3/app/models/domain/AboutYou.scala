package models.domain

import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.Postcode

case class AboutYou(yourDetails: YourDetails,
                    contactDetails: ContactDetails,
                    timeOutsideUK : Option[TimeOutsideUK],
                    claimDate : ClaimDate)

object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                       otherSurnames: Option[String], nationalInsuranceNumber: Option[NationalInsuranceNumber], nationality: String,
                       dateOfBirth: DayMonthYear, maritalStatus: String, alwaysLivedUK: String) extends QuestionGroup(YourDetails.id)

object YourDetails {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: MultiLineAddress, postcode: Option[String], phoneNumber: Option[String], mobileNumber: Option[String]) extends QuestionGroup(ContactDetails.id)

object ContactDetails {
  val id = s"${AboutYou.id}.g2"
}

case class TimeOutsideUK(currentlyLivingInUK: String, arrivedInUK: Option[DayMonthYear],
                         originCountry: Option[String], planToGoBack: Option[String], whenPlanToGoBack: Option[DayMonthYear],
                         visaReference: Option[String]) extends QuestionGroup(TimeOutsideUK.id)

object TimeOutsideUK {
  val id = s"${AboutYou.id}.g3"
}

case class ClaimDate(dateOfClaim: DayMonthYear) extends QuestionGroup(ClaimDate.id)

object ClaimDate {
  val id = s"${AboutYou.id}.g4"
}

case class MoreAboutYou(hadPartnerSinceClaimDate: String, eitherClaimedBenefitSinceClaimDate: String, beenInEducationSinceClaimDate: String, receiveStatePension: String) extends QuestionGroup(MoreAboutYou.id)

object MoreAboutYou {
  val id = s"${AboutYou.id}.g5"
}

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String, beenEmployedSince6MonthsBeforeClaim: String) extends QuestionGroup(Employment.id)

object Employment {
  val id = s"${AboutYou.id}.g6"
}

case class PropertyAndRent(ownProperty: String, hasSublet: String) extends QuestionGroup(PropertyAndRent.id)

object PropertyAndRent {
  val id = s"${AboutYou.id}.g7"
}