package xml

import app.XMLValues._
import models.domain.{CircumstancesYourContactDetails, CircumstancesAboutYou, DetailsOfThePersonYouCareFor, Circs}
import scala.xml.{NodeSeq, Elem}
import models.domain.Circs
import scala.Some
import xml.XMLHelper._
import models.domain.Circs
import scala.Some

object Identification {

  def xml(circs :Circs):NodeSeq = {

    <Claim>
      {claimant(circs)}
      {careeDetails(circs)}
    </Claim>


  }

  def claimant(circs: Circs): NodeSeq = {
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


  def careeDetails(circs: Circs):NodeSeq = {
    val detailsOfThePerson = circs.questionGroup[DetailsOfThePersonYouCareFor].getOrElse(DetailsOfThePersonYouCareFor())

    <CareeDetails>
      <OtherNames>{detailsOfThePerson.otherNames}</OtherNames>
      <Surname>{detailsOfThePerson.lastName}</Surname>
      <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
      <DateOfBirth>{detailsOfThePerson.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
    </CareeDetails>
  }

}
