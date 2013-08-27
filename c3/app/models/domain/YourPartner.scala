package models.domain

import models.{DayMonthYear, NationalInsuranceNumber}

object YourPartner extends Section.Identifier {
  val id = "s3"
}

case class YourPartnerPersonalDetails(title: String = "",
                                      firstName: String = "",
                                      middleName: Option[String] = None,
                                      surname: String = "",
                                      otherSurnames: Option[String] = None,
                                      nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                      dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                      nationality: Option[String] = None,
                                      separatedFromPartner: String = "") extends QuestionGroup(YourPartnerPersonalDetails)

object YourPartnerPersonalDetails extends QuestionGroup.Identifier  {
  val id = s"${YourPartner.id}.g1"
}

case class PersonYouCareFor(isPartnerPersonYouCareFor:String = "") extends QuestionGroup(PersonYouCareFor)

object PersonYouCareFor extends QuestionGroup.Identifier {
  val id = s"${YourPartner.id}.g4"
}