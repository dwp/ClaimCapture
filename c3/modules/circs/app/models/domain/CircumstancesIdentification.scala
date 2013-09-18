package models.domain

import models.{MultiLineAddress, DayMonthYear, NationalInsuranceNumber}


case object CircumstancesIdentification extends Section.Identifier {
  val id = "c1"
  //override val expectedMinTimeToCompleteInMillis: Long = 10000
}

case class CircumstancesAboutYou(title: String = "",
                    firstName: String = "",
                    middleName: Option[String] = None,
                    lastName: String = "",
                    nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                    dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                     ) extends QuestionGroup(CircumstancesAboutYou){
  def otherNames = firstName + middleName.map(" " + _).getOrElse("")

  override def numberOfCharactersInput: Int = {
    title.length +
    firstName.length +
    {middleName match {case Some(s) => s.length case _ => 0}} +
    lastName.length +
    nationalInsuranceNumber.numberOfCharactersInput +
    dateOfBirth.numberOfCharactersInput
  }
}

object CircumstancesAboutYou extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g1"
}

case class CircumstancesYourContactDetails(address: MultiLineAddress = MultiLineAddress(None, None, None),
                                           postcode: Option[String] = None,
                                           phoneNumber: Option[String] = None,
                                           mobileNumber: Option[String] = None
                                  ) extends QuestionGroup(CircumstancesYourContactDetails){
  override def numberOfCharactersInput: Int = {
    address.numberOfCharactersInput +
    {postcode match {case Some(s) => s.length case _ => 0}} +
    {phoneNumber match {case Some(s) => s.length case _ => 0}} +
    {mobileNumber match {case Some(s) => s.length case _ => 0}}
  }
}

object CircumstancesYourContactDetails extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g2"
}




case class DetailsOfThePersonYouCareFor(firstName: String = "",
                                 middleName: Option[String] = None,
                                 lastName: String = "",
                                 nationalInsuranceNumber: NationalInsuranceNumber = NationalInsuranceNumber(Some(""), Some(""), Some(""), Some(""), Some("")),
                                 dateOfBirth: DayMonthYear = DayMonthYear(None, None, None)
                                  ) extends QuestionGroup(DetailsOfThePersonYouCareFor){

  def otherNames = firstName + middleName.map(" " + _).getOrElse("")

  override def numberOfCharactersInput: Int = {
    firstName.length +
    {middleName match {case Some(s) => s.length case _ => 0}} +
    lastName.length +
    nationalInsuranceNumber.numberOfCharactersInput +
    dateOfBirth.numberOfCharactersInput
  }
}

object DetailsOfThePersonYouCareFor extends QuestionGroup.Identifier {
  val id = s"${CircumstancesIdentification.id}.g3"
}
