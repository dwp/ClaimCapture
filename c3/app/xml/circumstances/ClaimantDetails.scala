package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some
import xml.XMLComponent

object ClaimantDetails extends XMLComponent {

  def xml(circs :Claim): NodeSeq = {
    val yourDetails = circs.questionGroup[CircumstancesAboutYou].getOrElse(CircumstancesAboutYou())
    val contactDetails = circs.questionGroup[CircumstancesYourContactDetails].getOrElse(CircumstancesYourContactDetails())
    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <OtherSurnames>{yourDetails.middleName}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <DateOfBirth>{yourDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
      {postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}
      <DaytimePhonePhoneNumber>{contactDetails.phoneNumber.orNull}</DaytimePhonePhoneNumber>
      <MobileNumber>{contactDetails.mobileNumber.orNull}</MobileNumber>
    </ClaimantDetails>
  }
}