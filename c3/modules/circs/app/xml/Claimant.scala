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
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <OtherSurnames>{NotAsked}</OtherSurnames>
      <Title>{yourDetails.title}</Title>
      <MaritalStatus>{NotAsked}</MaritalStatus>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
      <ExistingNationalInsuranceNumber/>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress>
      <HomePhoneNumber>{contactDetails.phoneNumber.orNull}</HomePhoneNumber>
      <DaytimePhoneNumber>
        <Number>{contactDetails.mobileNumber.orNull}</Number>
        <Qualifier>mobile</Qualifier>
      </DaytimePhoneNumber>
      <EmailAddress/>
    </Claimant>
  }
}