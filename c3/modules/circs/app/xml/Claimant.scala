package xml

import app.XMLValues._
import models.domain._
import scala.xml.Elem
import xml.XMLHelper._
import models.domain.Circs

object Claimant {
  def xml(circs: Circs): Elem = {
    val yourDetails = circs.questionGroup[CircumstancesAboutYou].getOrElse(CircumstancesAboutYou())
    val contactDetails = circs.questionGroup[CircumstancesYourContactDetails].getOrElse(CircumstancesYourContactDetails())

    <Claimant>
      <DateOfClaim>{stringify(circs.dateOfClaim)}</DateOfClaim>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{stringify(yourDetails.middleName)}</OtherNames>
      <OtherSurnames>{NotAsked}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{NotAsked}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
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