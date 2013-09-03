package controllers

import utils.pageobjects.ClaimScenario


object CircumstancesScenarioFactory {

  def aboutDetails = {
    val claim = new ClaimScenario
    claim.CircumstancesAboutYouTitle = "mr"
    claim.CircumstancesAboutYouFirstName = "John"
    claim.CircumstancesAboutYouMiddleName = "Roger"
    claim.CircumstancesAboutYouLastName = "Smith"
    claim.CircumstancesAboutYouNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesAboutYouDateOfBirth = "03/04/1950"
    claim
  }

  def yourContactDetails = {
    val claim = new ClaimScenario
    claim.CircumstancesYourContactDetailsAddress = "101 Clifton Street&Blackpool"
    claim.CircumstancesYourContactDetailsPostcode = "PE1 4AQ"
    claim.CircumstancesYourContactDetailsPhoneNumber = "123456"
    claim.CircumstancesYourContactDetailsMobileNumber = "34343434"
    claim
  }

  def detailsOfThePersonYouCareFor = {
    val claim = new ClaimScenario
    claim.CircumstancesDetailsOfThePersonYouCareForTitle = "mr"
    claim.CircumstancesDetailsOfThePersonYouCareForFirstName = "John"
    claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = "Roger"
    claim.CircumstancesDetailsOfThePersonYouCareForLastName = "Smith"
    claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = "03/04/1950"
    claim
  }

  def otherChangeInfo = {
    val claim = new ClaimScenario
    claim.CircumstancesOtherChangeInfoChange
    claim
  }

}
