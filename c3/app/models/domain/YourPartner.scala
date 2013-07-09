package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}

case class YourPartner(yourPartnerPersonalDetails: YourPartnerPersonalDetails, yourPartnerContactDetails: YourPartnerContactDetails,
                          moreAboutYourPartner: MoreAboutYourPartner, personYouCareFore: Option[PersonYouCareFor])
                          
object YourPartner {
  val id = "s3"
}

case class YourPartnerPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String, otherNames: Option[String], nationalInsuranceNumber: Option[NationalInsuranceNumber], dateOfBirth: DayMonthYear, nationality: Option[String], liveAtSameAddress: String) extends QuestionGroup(YourPartnerPersonalDetails.id)

object YourPartnerPersonalDetails extends QuestionGroup(s"${YourPartner.id}.g1")

case class YourPartnerContactDetails(address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(YourPartnerContactDetails.id)

object YourPartnerContactDetails extends QuestionGroup(s"${YourPartner.id}.g2")

case class MoreAboutYourPartner(dateStartedLivingTogether: Option[DayMonthYear], separatedFromPartner: String, separationDate: Option[DayMonthYear]) extends QuestionGroup(MoreAboutYourPartner.id)

object MoreAboutYourPartner extends QuestionGroup(s"${YourPartner.id}.g3")

case class PersonYouCareFor(isPartnerPersonYouCareFor:String) extends QuestionGroup(PersonYouCareFor.id)

object PersonYouCareFor extends QuestionGroup(s"${YourPartner.id}.g4")