package xml.circumstances

import scala.xml.NodeSeq
import xml.XMLHelper._
import scala.Some
import models.domain.{CircumstancesDeclaration, CircumstancesReportChange, Claim}

object CircsIdentification {

  def xml(circs :Claim): NodeSeq = {
      {claimant(circs)}
      {careeDetails(circs)}
  }

  def claimant(circs: Claim): NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())
    val contactPreference = circs.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())

    println("claimant info: ")

    <ClaimantDetails>
      <FullName>{reportChange.fullName}</FullName>
      <DateOfBirth>{reportChange.dateOfBirth.`yyyy-MM-dd`}</DateOfBirth>
      <NationalInsuranceNumber>{stringify(Some(reportChange.nationalInsuranceNumber))}</NationalInsuranceNumber>
      <ContactPreference>{contactPreference.furtherInfoContact}</ContactPreference>
    </ClaimantDetails>
  }

  def careeDetails(circs: Claim):NodeSeq = {
    val reportChange = circs.questionGroup[CircumstancesReportChange].getOrElse(CircumstancesReportChange())

    <CareeDetails>
      <FullName>{reportChange.theirFullName}</FullName>
      {question(<RelationToClaimant/>,"theirRelationshipToYou", reportChange.theirRelationshipToYou)}
    </CareeDetails>
  }
}