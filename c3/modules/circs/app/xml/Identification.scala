package xml

import models.domain.{CircumstancesYourContactDetails, CircumstancesAboutYou, DetailsOfThePersonYouCareFor}
import scala.xml.NodeSeq
import xml.XMLHelper._
import models.domain.Circs
import scala.Some
import app.XMLValues._

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

    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
      <Title>{yourDetails.title}</Title>
      <Address>{postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}</Address>
      <ConfirmAddress>{yes}</ConfirmAddress>
      <HomePhone>{contactDetails.phoneNumber.orNull}</HomePhone>
      <DaytimePhone>
        <Number>{contactDetails.mobileNumber.orNull}</Number>
        <Qualifier>{if (contactDetails.mobileNumber.isDefined){"mobile"}else{""}}</Qualifier>
      </DaytimePhone>
      <EmailAddress/>
    </ClaimantDetails>
  }


  def careeDetails(circs: Circs):NodeSeq = {
    val detailsOfThePerson = circs.questionGroup[DetailsOfThePersonYouCareFor].getOrElse(DetailsOfThePersonYouCareFor())

    <CareeDetails>
      <Surname>{detailsOfThePerson.lastName}</Surname>
      <OtherNames>{detailsOfThePerson.otherNames}</OtherNames>
      <DateOfBirth>{detailsOfThePerson.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
    </CareeDetails>
  }

}
