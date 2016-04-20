package models.domain


import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}

object AboutYou extends Section.Identifier {
  val id = "s3"
}

case class YourDetails(title: String = "",
                       firstName: String = "",
                       middleName: Option[String] = None,
                       surname: String = "",
                       nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(None),
                       dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)) extends QuestionGroup(YourDetails) {

  def otherNames = firstName + middleName.map(" " + _).getOrElse("")
}

object YourDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g1"
}

case class MaritalStatus(maritalStatus: String = "") extends QuestionGroup(MaritalStatus)

object MaritalStatus extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g2"
}

case class ContactDetails(address: MultiLineAddress = new MultiLineAddress(),
                          postcode: Option[String] = None,
                          howWeContactYou: Option[String] = None,
                          contactYouByTextphone: Option[String] = None,
                          override val wantsContactEmail: String = "",
                          override val email: Option[String] = None,
                          override val emailConfirmation: Option[String] = None) extends QuestionGroup(ContactDetails) with EMail

object ContactDetails extends QuestionGroup.Identifier {
  val id = s"${AboutYou.id}.g3"
}

