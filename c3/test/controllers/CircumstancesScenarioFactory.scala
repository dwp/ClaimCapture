package controllers

import utils.pageobjects.TestData
import app.ReportChange._
import play.api.i18n.Messages

object CircumstancesScenarioFactory {

  def aboutDetails = {
    val claim = new TestData
    claim.CircumstancesAboutYouFullName = "Mr John Roger Smith"
    claim.CircumstancesAboutYouNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesAboutYouDateOfBirth = "03/04/1950"
    claim.CircumstancesAboutYouTheirFullName = "Mrs Jane Smith"
    claim.CircumstancesAboutYouTheirRelationshipToYou = "Wife"
    claim
  }

  def reportChangesSelfEmployment = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = SelfEmployment.name
    claim
  }

  def reportChangesStoppedCaring = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = StoppedCaring.name
    claim
  }

  def reportChangesOtherChangeInfo = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = AdditionalInfo.name
    claim
  }

  def otherChangeInfo = {
    val claim = reportChangesOtherChangeInfo
    claim.CircumstancesOtherChangeInfoChange = "I put in the wrong date of birth"
    claim
  }

  def declaration = {
    val claim = otherChangeInfo
    claim.FurtherInfoContact = "By Post"
    claim.CircumstancesDeclarationInfoAgreement = "yes"
    claim.CircumstancesDeclarationWhy = "Cause I want"
    claim.CircumstancesDeclarationConfirmation = "yes"
    claim.CircumstancesSomeOneElseConfirmation = "yes"
    claim.NameOrOrganisation = "Mr Smith"
    claim
  }

}
