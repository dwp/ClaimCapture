package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLComponent

object ClaimantDetails extends XMLComponent {

  def xml(circs :Claim): NodeSeq = {
    // TODO : Schema and in turn the followwing xml needs to be changed to reflect the new changes : Prafulla Chandra
    /*
    val yourDetails = circs.questionGroup[CircumstancesAboutYou].getOrElse(CircumstancesAboutYou())
    val contactDetails = circs.questionGroup[CircumstancesYourContactDetails].getOrElse(CircumstancesYourContactDetails())
    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.firstName} {yourDetails.middleName.getOrElse("")}</OtherNames>
      <Title>{yourDetails.title}</Title>
      <DateOfBirth>{yourDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      {statement(<NationalInsuranceNumber/>,yourDetails.nationalInsuranceNumber)}
      {postalAddressStructure(contactDetails.address, contactDetails.postcode)}
      {statement(<DayTimePhoneNumber/>,contactDetails.phoneNumber)}
      {statement(<MobileNumber/>,contactDetails.mobileNumber)}
    </ClaimantDetails>
    */
    NodeSeq.Empty
  }
}