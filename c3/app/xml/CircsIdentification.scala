package xml

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import app.XMLValues._
import scala.Some
import models.MultiLineAddress

object CircsIdentification {

  def xml(circs :Claim): NodeSeq = {
    <Claim>
      {claimant(circs)}
      {careeDetails(circs)}
    </Claim>
  }

  def claimant(circs: Claim): NodeSeq = {
    val yourDetails = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <ClaimantDetails>
      <Surname>{yourDetails.lastName}</Surname>
      <OtherNames>{yourDetails.otherNames}</OtherNames>
      <DateOfBirth>{yourDetails.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(yourDetails.nationalInsuranceNumber))}</NationalInsuranceNumber>
      <Title>{yourDetails.title}</Title>
      <Address>{postalAddressStructure(None, None)}</Address>
      <ConfirmAddress>{NotAsked}</ConfirmAddress>
      <HomePhone>{NotAsked}</HomePhone>
      <DaytimePhone>
        <Number></Number>
        <Qualifier></Qualifier>
      </DaytimePhone>
      <EmailAddress/>
    </ClaimantDetails>
  }

  def careeDetails(circs: Claim):NodeSeq = {
    <CareeDetails>
      <Surname>{NotAsked}</Surname>
      <OtherNames>{NotAsked}</OtherNames>
      <DateOfBirth>{NotAsked}</DateOfBirth>
      <NationalInsuranceNumber>{NotAsked}</NationalInsuranceNumber>
    </CareeDetails>
  }
}