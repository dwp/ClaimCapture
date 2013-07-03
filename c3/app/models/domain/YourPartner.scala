package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}

object YourPartner {
  val id = "s3"
}

case class YourPartnerPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String, otherNames: Option[String], nationalInsuranceNumber: Option[NationalInsuranceNumber], dateOfBirth: DayMonthYear, nationality: Option[String], liveAtSameAddress: String) extends QuestionGroup(YourPartnerPersonalDetails.id)

object YourPartnerPersonalDetails {
  val id = s"${YourPartner.id}.g1"
}

case class YourPartnerContactDetails(address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(YourPartnerContactDetails.id)

object YourPartnerContactDetails {
  val id = s"${YourPartner.id}.g2"
}


case class MoreAboutYourPartner(dateStartedLivingTogether: DayMonthYear, separatedFromPartner: String) extends QuestionGroup(MoreAboutYourPartner.id)

object MoreAboutYourPartner {
  val id = s"${YourPartner.id}.g3"
}


