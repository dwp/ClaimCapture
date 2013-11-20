package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some
import xml.XMLComponent

object Identification extends XMLComponent {

  def xml(circs :Claim): NodeSeq = {
      {claimant(circs)}
      {careeDetails(circs)}
  }


  def claimant(circs: Claim): NodeSeq = {
    val yourDetails = circs.questionGroup[CircumstancesAboutYou].getOrElse(CircumstancesAboutYou())
    val contactDetails = circs.questionGroup[CircumstancesYourContactDetails].getOrElse(CircumstancesYourContactDetails())

    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <Title>{yourDetails.title}</Title>
      <DateOfBirth>{yourDetails.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
      {postalAddressStructure(contactDetails.address, contactDetails.postcode.orNull)}
      <DaytimePhone>{contactDetails.phoneNumber.orNull}</DaytimePhone>
      <HomePhone>{contactDetails.mobileNumber.orNull}</HomePhone>
    </ClaimantDetails>
  }

  def careeDetails(circs: Claim):NodeSeq = {
    val detailsOfThePerson = circs.questionGroup[DetailsOfThePersonYouCareFor].getOrElse(DetailsOfThePersonYouCareFor())

    <CareeDetails>
      <Surname>{detailsOfThePerson.lastName}</Surname>
      <OtherNames>{detailsOfThePerson.otherNames}</OtherNames>
      <DateOfBirth>{detailsOfThePerson.dateOfBirth.`dd-MM-yyyy`}</DateOfBirth>
      <NationalInsuranceNumber>{detailsOfThePerson.nationalInsuranceNumber.stringify}</NationalInsuranceNumber>
    </CareeDetails>
  }
}