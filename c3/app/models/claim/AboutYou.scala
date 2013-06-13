package models.claim

import models.DayMonthYear

object AboutYou {
  val id = "s2"
}

case class YourDetails(title: String, firstName: String, middleName: Option[String], surname: String,
                       otherNames: Option[String], nationalInsuranceNumber: Option[String], nationality: String,
                       dateOfBirth: DayMonthYear, maritalStatus: String, alwaysLivedUK: String) extends Form(YourDetails.id)

object YourDetails {
  val id = s"${AboutYou.id}.g1"
}

case class ContactDetails(address: String, postcode: String, phoneNumber: Option[String], mobileNumber: Option[String]) extends Form(ContactDetails.id)

object ContactDetails {
  val id = s"${AboutYou.id}.g2"
}

case class ClaimDate(dateOfClaim: DayMonthYear) extends Form(ClaimDate.id)

object ClaimDate {
  val id = s"${AboutYou.id}.g4"
}

case class MoreAboutYou(hadPartnerSinceClaimDate: String,eitherClaimedBenefitSinceClaimDate: String,beenInEducationSinceClaimDate: String,receiveStatePension: String) extends Form(MoreAboutYou.id)

object MoreAboutYou {
  val id = s"${AboutYou.id}.g5"
}

case class Employment(beenSelfEmployedSince1WeekBeforeClaim: String,beenEmployedSince6MonthsBeforeClaim: String) extends Form(Employment.id)

object Employment {
  val id = s"${AboutYou.id}.g6"
}

case class PropertyAndRent(ownProperty: String,hasSublet: String) extends Form(PropertyAndRent.id)

object PropertyAndRent {
  val id = s"${AboutYou.id}.g7"
}