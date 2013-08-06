package models.domain

import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import models.yesNo.YesNoWithDate

case class YourPartner(yourPartnerPersonalDetails: YourPartnerPersonalDetails, yourPartnerContactDetails: YourPartnerContactDetails,
                       moreAboutYourPartner: MoreAboutYourPartner, personYouCareFor: Option[PersonYouCareFor])

object YourPartner extends Section.Identifier {
  val id = "s3"
}

case class YourPartnerPersonalDetails(title: String, firstName: String, middleName: Option[String], surname: String, otherNames: Option[String],
                                      nationalInsuranceNumber: Option[NationalInsuranceNumber], dateOfBirth: DayMonthYear,
                                      nationality: Option[String], liveAtSameAddress: String) extends QuestionGroup(YourPartnerPersonalDetails) with NoRouting

object YourPartnerPersonalDetails extends QuestionGroup.Identifier  {
  val id = s"${YourPartner.id}.g1"
}

case class YourPartnerContactDetails(address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(YourPartnerContactDetails) with NoRouting

object YourPartnerContactDetails extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g2"
}

case class MoreAboutYourPartner(startedLivingTogether: Option[YesNoWithDate], separated:YesNoWithDate) extends QuestionGroup(MoreAboutYourPartner) with NoRouting

object MoreAboutYourPartner extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g3"
}

case class PersonYouCareFor(isPartnerPersonYouCareFor:String) extends QuestionGroup(PersonYouCareFor) with NoRouting

object PersonYouCareFor extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g4"
}