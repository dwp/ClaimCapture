package xml

import app.XMLValues._
import models.domain.{ContactDetails, YourDetails, Claim}
import scala.xml.Elem
import xml.XMLHelper._

object Claimant {
  def xml(claim: Claim): Elem = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())

    <Claimant>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      {if(!yourDetails.otherSurnames.isEmpty) <OtherSurnames>{yourDetails.otherSurnames.orNull}</OtherSurnames> }
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{yourDetails.maritalStatus}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{yourDetails.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber/>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress>
      <HomePhoneNumber></HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{contactDetails.phoneNumber.orNull}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>{NotAsked}</ClaimedBefore>
    </Claimant>
  }
}