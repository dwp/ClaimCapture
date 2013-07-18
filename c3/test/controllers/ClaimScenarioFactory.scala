package controllers

import utils.pageobjects.{FactoryFromFile, ClaimScenario}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
object ClaimScenarioFactory {


  val partnerAddress = "Partner Address"
  val partnerPostcode = "RM11 1AA"

  def buildClaimFromFile(fileName: String) = {
    val claim = new ClaimScenario
    FactoryFromFile.buildFromFile(fileName, claim.updateDynamic)
    claim
  }

  def yourDetailsWithNotTimeOutside() = {
    val claim = new ClaimScenario
    claim.AboutYouTitle = "Mr"
    claim.AboutYouFirstName = "John"
    claim.AboutYouSurname = "Appleseed"
    claim.AboutYouNationality = "English"
    claim.AboutYouDateOfBirth = "03/04/1950"
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"
    claim.AboutYouHaveYouAlwaysLivedInTheUK = "Yes"
    claim.AboutYouNINO ="AB123456C"
    claim
  }

  def yourDetailsEnablingTimeOutsideUK() = {
    val claim = yourDetailsWithNotTimeOutside
    claim.AboutYouHaveYouAlwaysLivedInTheUK = "No"
    claim.AboutYouAreYouCurrentlyLivingintheUk = "Yes"
    claim.AboutYouWhenDidYouArriveInYheUK = "01/11/2003"
    claim.AboutYouDoYouPlantoGoBacktoThatCountry = "No"
    claim
  }

  def s2AboutYouWithTimeOutside() = {
    // Your details + outside UK
    val claim = yourDetailsEnablingTimeOutsideUK()
    // Your contact details
    claim.AboutYouAddress  = "An address"
    claim.AboutYouPostcode = "SE1 6EH"
    claim.AboutYouDaytimePhoneNumber = "01253 111 111"
    claim.AboutYouMobileNumber = "07111 111 111"
    // Claim date
    claim.AboutYouWhenDoYouWantYourCarersAllowanceClaimtoStart = "03/05/2014"
    // More about you
    claim.AboutYouHaveYouHadaPartnerSpouseatAnyTime = "Yes"
    claim.AboutYouHaveYouOrYourPartnerSpouseClaimedorReceivedAnyOtherBenefits = "Yes"
    claim.AboutYouHaveYouBeenOnACourseOfEducation = "Yes"
    claim.AboutYouDoYouGetStatePension = "Yes"
    // Employment
    claim.AboutYouHaveYouBeenSelfEmployedAtAnyTime = "Yes"
    claim.AboutYouHaveYouBeenEmployedAtAnyTime = "Yes"
    // Property and Rent
    claim.AboutYouDoYouOrYourPartnerSpouseOwnPropertyorLand = "Yes"
    claim.AboutYouHaveYouOrYourPartnerSubletYourHome = "Yes"
    claim
  }


}
