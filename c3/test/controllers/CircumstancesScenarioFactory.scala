package controllers

import utils.pageobjects.{TestData, ClaimScenario}


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
    claim.CircumstancesYourContactDetailsPhoneNumber = "123456"
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

  def otherChangeInfo = {
    val claim = detailsOfThePersonYouCareFor
    claim.CircumstancesOtherChangeInfoChange = "I put in the wrong date of birth"
    claim
  }

  def declaration = {
    val claim = otherChangeInfo
    claim.CircumstancesDeclarationInfoAgreement = "yes"
    claim.CircumstancesDeclarationWhy = "Cause I want"
    claim.CircumstancesDeclarationConfirmation = "yes"
    claim
  }

}
