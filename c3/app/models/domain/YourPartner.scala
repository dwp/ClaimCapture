package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}

object YourPartner {
  val id = "s3"
}

case class YourPartnerPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String, otherNames: Option[String], nationalInsuranceNumber: Option[NationalInsuranceNumber], dateOfBirth: DayMonthYear, nationality: Option[String], liveAtSameAddress: String) extends QuestionGroup(YourPartnerPersonalDetails.id)

object YourPartnerPersonalDetails {
  val id = s"${YourPartner.id}.g1"
}

case class YourPartnerContactDetails(address: Option[MultiLineAddress]) extends QuestionGroup(YourPartnerContactDetails.id)

object YourPartnerContactDetails {
  val id = s"${YourPartner.id}.g2"
}


