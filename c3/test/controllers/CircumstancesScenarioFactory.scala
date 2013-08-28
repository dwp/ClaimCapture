package controllers

import utils.pageobjects.ClaimScenario


object CircumstancesScenarioFactory {

  def aboutDetails = {
    val claim = new ClaimScenario
    claim.CircumstancesAboutYouTitle = "Mr"
    claim.CircumstancesAboutYouFirstName = "John"
    claim.CircumstancesAboutYouMiddleName = "Roger"
    claim.CircumstancesAboutYouLastName = "Smith"
    claim.CircumstancesAboutYouNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesAboutYouDateOfBirth = "03/04/1950"
    claim
  }

}
