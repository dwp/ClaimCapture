package xml.claim

import app.XMLValues._
import models.domain.{MoreAboutYou, ContactDetails, YourDetails, Claim}
import scala.xml.Elem
import xml.XMLHelper._

object Claimant {
  def xml(claim: Claim): Elem = {
    val yourDetails = claim.questionGroup[YourDetails].getOrElse(YourDetails())
    val moreAboutYou = claim.questionGroup[MoreAboutYou].getOrElse(MoreAboutYou())
    val contactDetails = claim.questionGroup[ContactDetails].getOrElse(ContactDetails())

    <Claimant>
      <DateOfClaim>{stringify(claim.dateOfClaim)}</DateOfClaim>
      <Surname>{yourDetails.surname}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <OtherSurnames>{yourDetails.otherSurnames.orNull}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{moreAboutYou.maritalStatus}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{yourDetails.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber/>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress>
      <HomePhoneNumber></HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{contactDetails.howWeContactYou}</Number>
        <Qualifier/>
      </DaytimePhoneNumber>
      <EmailAddress/>
      <ClaimedBefore>{NotAsked}</ClaimedBefore>
    </Claimant>
  }
}