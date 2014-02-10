package controllers

import utils.pageobjects.TestData
import app.ReportChange._
import play.api.i18n.Messages

object CircumstancesScenarioFactory {

  def aboutDetails = {
    val claim = new TestData
    claim.CircumstancesAboutYouTitle = "mr"
    claim.CircumstancesAboutYouFirstName = "John"
    claim.CircumstancesAboutYouMiddleName = "Roger"
    claim.CircumstancesAboutYouLastName = "Smith"
    claim.CircumstancesAboutYouNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesAboutYouDateOfBirth = "03/04/1950"
    claim
  }

  def yourContactDetails = {
    val claim = aboutDetails
    claim.CircumstancesYourContactDetailsAddress = "101 Clifton Street&Blackpool"
    claim.CircumstancesYourContactDetailsPostcode = "PE1 4AQ"
    claim.CircumstancesYourContactDetailsPhoneNumber = "01772700806"
    claim.CircumstancesYourContactDetailsMobileNumber = "34343434"
    claim
  }

  def detailsOfThePersonYouCareFor = {
    val claim = yourContactDetails
    claim.CircumstancesDetailsOfThePersonYouCareForFirstName = "John"
    claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = "Roger"
    claim.CircumstancesDetailsOfThePersonYouCareForLastName = "Smith"
    claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = "03/04/1950"
    claim
  }

  def reportChangesSelfEmployment = {
    val claim = detailsOfThePersonYouCareFor
    claim.CircumstancesReportChanges = SelfEmployment.name
    claim
  }

  def reportChangesStoppedCaring = {
    val claim = detailsOfThePersonYouCareFor
    claim.CircumstancesReportChanges = StoppedCaring.name
    claim
  }

  def reportChangesOtherChangeInfo = {
    val claim = detailsOfThePersonYouCareFor
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
