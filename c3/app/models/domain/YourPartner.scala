package models.domain

import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import play.api.mvc.Call
import models.yesNo.YesNoWithDate

case class YourPartner(yourPartnerPersonalDetails: YourPartnerPersonalDetails, yourPartnerContactDetails: YourPartnerContactDetails,
                       moreAboutYourPartner: MoreAboutYourPartner, personYouCareFor: Option[PersonYouCareFor])

object YourPartner extends Section.Identifier {
  val id = "s3"
}

case class YourPartnerPersonalDetails(call: Call,
                                      title: String, firstName: String, middleName: Option[String], surname: String, otherNames: Option[String],
                                      nationalInsuranceNumber: Option[NationalInsuranceNumber], dateOfBirth: DayMonthYear,
                                      nationality: Option[String], liveAtSameAddress: String) extends QuestionGroup(YourPartnerPersonalDetails)

object YourPartnerPersonalDetails extends QuestionGroup.Identifier  {
  val id = s"${YourPartner.id}.g1"
}

case class YourPartnerContactDetails(call: Call, address: Option[MultiLineAddress], postcode: Option[String]) extends QuestionGroup(YourPartnerContactDetails)

object YourPartnerContactDetails extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g2"
}

case class MoreAboutYourPartner(call: Call, startedLivingTogether: Option[YesNoWithDate], separated:YesNoWithDate) extends QuestionGroup(MoreAboutYourPartner)

object MoreAboutYourPartner extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g3"
}

case class PersonYouCareFor(call: Call,
                            isPartnerPersonYouCareFor:String) extends QuestionGroup(PersonYouCareFor)

object PersonYouCareFor extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g4"
}