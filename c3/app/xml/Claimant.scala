package xml

import models.domain.{ContactDetails, YourDetails, Claim}
import scala.xml.Elem
import xml.XMLHelper._

object Claimant {
  def xml(claim: Claim): Elem = {
    val yourDetailsOption = claim.questionGroup[YourDetails]
    val yourDetails = yourDetailsOption.getOrElse(YourDetails())

    val contactDetailsOption = claim.questionGroup[ContactDetails]
    val contactDetails = contactDetailsOption.getOrElse(ContactDetails())

    <Claimant>
      <DateOfClaim>{stringify(claim.dateOfClaim)}</DateOfClaim>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <OtherSurnames>{yourDetails.otherSurnames.orNull}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{yourDetails.maritalStatus}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(yourDetails.nationalInsuranceNumber)}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber/>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>yes</ConfirmAddress>
      <HomePhoneNumber>{contactDetails.mobileNumber.orNull}</HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{contactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>no</ClaimedBefore>
    </Claimant>
  }
}