package xml.circumstances

import models.domain._
import scala.xml.NodeSeq
import xml.XMLHelper._
import app.XMLValues._
import scala.Some

object CircsIdentification {

  def xml(circs :Claim): NodeSeq = {
    <Claim>
      {claimant(circs)}
      {careeDetails(circs)}
    </Claim>
  }

  def claimant(circs: Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <ClaimantDetails>
      <Surname>{NotAsked}</Surname>
      <OtherNames>{reportChange.fullName}</OtherNames>
      <DateOfBirth>{reportChange.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(reportChange.nationalInsuranceNumber))}</NationalInsuranceNumber>
      <Title>{NotAsked}</Title>
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
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <CareeDetails>
      <Surname>{NotAsked}</Surname>
      <OtherNames>{reportChange.theirFullName}</OtherNames>
      <DateOfBirth>{NotAsked}</DateOfBirth>
      <NationalInsuranceNumber>{NotAsked}</NationalInsuranceNumber>
    </CareeDetails>
  }
}