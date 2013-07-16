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

}
