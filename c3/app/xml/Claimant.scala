package xml

import models.domain.{ContactDetails, YourDetails, Claim}
import scala.xml.Elem
import xml.XMLHelper._

object Claimant {
  def xml(claim: Claim): Elem = {
    val yourDetails = claim.questionGroup[YourDetails].get
    val contactDetails = claim.questionGroup[ContactDetails].get

    <Claimant>
      <DateOfClaim>{claim.dateOfClaim.get.`yyyy-MM-dd`}</DateOfClaim>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <OtherSurnames>{yourDetails.otherSurnames.orNull}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{yourDetails.maritalStatus}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{yourDetails.nationalInsuranceNumber.orNull}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber>no</ExistingNationalInsuranceNumber>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>yes</ConfirmAddress> <!-- Always default to yes -->
      <HomePhoneNumber>{contactDetails.mobileNumber.orNull}</HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{contactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>no</ClaimedBefore> <!--  Default to no -->
    </Claimant>
  }
}