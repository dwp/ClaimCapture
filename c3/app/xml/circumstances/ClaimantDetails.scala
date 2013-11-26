package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import xml.XMLComponent

object ClaimantDetails extends XMLComponent {

  def xml(circs :Claim): NodeSeq = {
    val yourDetails = circs.questionGroup[CircumstancesAboutYou].getOrElse(CircumstancesAboutYou())
    val contactDetails = circs.questionGroup[CircumstancesYourContactDetails].getOrElse(CircumstancesYourContactDetails())
    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.firstName} {yourDetails.middleName.getOrElse("")}</OtherNames>
      <Title>{yourDetails.title}</Title>
      <DateOfBirth>{yourDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(yourDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
      {postalAddressStructure(contactDetails.address, contactDetails.postcode)}
      {<DayTimePhoneNumber/> ?+ contactDetails.phoneNumber}
      {<MobileNumber/> ?+ contactDetails.mobileNumber}
    </ClaimantDetails>
  }
}