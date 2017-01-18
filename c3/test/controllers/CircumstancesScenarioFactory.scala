package controllers

import controllers.mappings.Mappings
import utils.pageobjects.TestData
import app.ReportChange._
import app.{CircsBreaksWhereabouts, WhoseNameAccount, PaymentFrequency}

object CircumstancesScenarioFactory {
  val yes = "yes"
  val no = "no"

  def aboutDetails = {
    val claim = new TestData
    claim.CircumstancesAboutYouFirstName = "Mr John Roger"
    claim.CircumstancesAboutYouSurname = "Smith"
    claim.CircumstancesAboutYouNationalInsuranceNumber = "AB123456C"
    claim.CircumstancesAboutYouDateOfBirth = "03/04/1950"
    claim.CircumstancesAboutYouTheirFirstName = "Mrs Jane"
    claim.CircumstancesAboutYouTheirSurname = "Johnson"
    claim.CircumstancesAboutYouTheirRelationshipToYou = "Wife"

    claim.FurtherInfoContact = "012345678"
    claim.CircumstancesDeclarationWantsEmailContact = "no"

    claim
  }

  def reportChangesSelfEmployment = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = EmploymentChange.name
    claim
  }

  def reportChangesEmploymentChangeSelfEmploymentNotStartedYet = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = EmploymentChange.name

    claim.CircumstancesEmploymentChangeStillCaring = "no"
    claim.CircumstancesEmploymentChangeFinishedStillCaringDate = "03/04/2014"
    claim.CircumstancesEmploymentChangeHasWorkStartedYet = "no"
    claim.CircumstancesEmploymentChangeDateWhenWillItStart = "03/04/2099"
    claim.CircumstancesEmploymentChangeTypeOfWork = "self-employed"
    claim.CircumstancesEmploymentChangeSelfEmployedTypeOfWork = "IT Consultant"
    claim.CircumstancesEmploymentChangeSelfEmployedTotalIncome = "no"
    claim.CircumstancesEmploymentChangeSelfEmployedPaidMoneyYet = "no"

    claim.FurtherInfoContact = "0171123455"
    claim.CircumstancesDeclarationWantsEmailContact = "no"


    claim
  }

  def reportChangesEmploymentChangeSelfEmploymentStartedAndOngoing = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = EmploymentChange.name

    claim.CircumstancesEmploymentChangeStillCaring = "yes"
    claim.CircumstancesEmploymentChangeHasWorkStartedYet = "yes"
    claim.CircumstancesEmploymentChangeDateWhenStarted = "03/04/2014"
    claim.CircumstancesEmploymentChangeHasWorkFinishedYet = "no"
    claim.CircumstancesEmploymentChangeTypeOfWork = "self-employed"
    claim.CircumstancesEmploymentChangeSelfEmployedTypeOfWork = "IT Consultant"
    claim.CircumstancesEmploymentChangeSelfEmployedTotalIncome = "yes"
    claim.CircumstancesEmploymentChangeSelfEmployedPaidMoneyYet = "no"

    claim
  }

  def reportChangesEmploymentChangeEmploymentPresent = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = EmploymentChange.name
    claim.CircumstancesEmploymentChangeStillCaring = Mappings.yes
    claim.CircumstancesEmploymentChangeHasWorkStartedYet = Mappings.yes
    claim.CircumstancesEmploymentChangeDateWhenStarted = "01/01/2013"
    claim.CircumstancesEmploymentChangeHasWorkFinishedYet = Mappings.no
    claim.CircumstancesEmploymentChangeTypeOfWork = "employed"
    claim.CircumstancesEmploymentChangeEmployerName = "Asda"
    claim.CircumstancesEmploymentChangeEmployerNameAndAddress = "Fulwood&Preston"
    claim.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"

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

  def paymentChangesChangeInfo = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = PaymentChange.name
    claim
  }

  def addressChange = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = AddressChange.name
    claim
  }

  def otherChangeInfo = {
    val claim = reportChangesOtherChangeInfo
    claim.CircumstancesOtherChangeInfoChange = "I put in the wrong date of birth"
    claim
  }

  def reportBreakFromCaring = {
    val claim = aboutDetails
    claim.CircumstancesReportChanges = BreakFromCaring.name
    claim
  }

  def reportChangesAddressChangeYes = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "yes"
    claim.CircumstancesAddressChangeNewAddress = "1 new address lane&2 new address lane"
    claim.CircumstancesAddressChangeNewPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeCaredForChangedAddress = "yes"
    claim.CircumstancesAddressChangeSameAddress = "no"
    claim.CircumstancesAddressChangeSameAddressTheirAddress ="1 test lane&2 test lane"
    claim.CircumstancesAddressChangeSameAddressTheirPostcode ="PR1A4JQ"
    claim.CircumstancesAddressChangeMoreAboutChanges ="Additional info about changes"

    claim
  }

  def reportChangesAddressChangeNo = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "no"
    claim.CircumstancesAddressChangeFinishedStillCaringDate = "03/04/2013"
    claim.CircumstancesAddressChangeNewAddress = "1 new address lane&2 new address lane"
    claim.CircumstancesAddressChangeNewPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeMoreAboutChanges ="Additional info about changes"

    claim
  }

  def reportChangeAddressMissingPersonChangedAddress = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "yes"
    claim.CircumstancesAddressChangeNewAddress = "1 new address lane&2 new address lane"
    claim.CircumstancesAddressChangeNewPostcode = "PR1A4JQ"

    claim
  }

  def reportChangeAddressMissingDateStoppedCaring = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "no"
    claim.CircumstancesAddressChangeNewAddress = "1 new address lane&2 new address lane"
    claim.CircumstancesAddressChangeNewPostcode = "PR1A4JQ"

    claim
  }

  def reportChangeAddressMissingNewAddress = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "no"
    claim.CircumstancesAddressChangeFinishedStillCaringDate = "03/04/2013"

    claim
  }

  def reportChangeAddressMissingNewAddressAndDate = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "no"

    claim
  }

  def reportChangesAddressMissingSameAddress = {
    val claim = addressChange

    claim.CircumstancesAddressChangePreviousAddress = "1 test lane&2 test lane"
    claim.CircumstancesAddressChangePreviousPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeStillCaring = "yes"
    claim.CircumstancesAddressChangeNewAddress = "1 new address lane&2 new address lane"
    claim.CircumstancesAddressChangeNewPostcode = "PR1A4JQ"
    claim.CircumstancesAddressChangeCaredForChangedAddress = "yes"

    claim
  }

  def reportChangesPaymentChangeScenario1 = {
    val claim = paymentChangesChangeInfo

    claim.CircumstancesPaymentChangeCurrentlyPaidIntoBank = yes
    claim.CircumstancesPaymentChangeNameOfCurrentBank = "Nat West"
    claim.CircumstancesPaymentChangeAccountHolderName = "Mr John Doe"
    claim.CircumstancesPaymentChangeWhoseNameIsTheAccountIn = WhoseNameAccount.YourName
    claim.CircumstancesPaymentChangeBankFullName = "HSBC"
    claim.CircumstancesPaymentChangeSortCode = "112233"
    claim.CircumstancesPaymentChangeAccountNumber = "12345678"
    claim.CircumstancesPaymentChangePaymentFrequency = PaymentFrequency.EveryWeek

    claim
  }

  def reportChangesPaymentChangeScenario2 = {
    val claim = paymentChangesChangeInfo

    claim.CircumstancesPaymentChangeCurrentlyPaidIntoBank = no
    claim.CircumstancesPaymentCurrentPaymentMethod = "Cheque"
    claim.CircumstancesPaymentChangeAccountHolderName = "Mr John Doe"
    claim.CircumstancesPaymentChangeWhoseNameIsTheAccountIn = WhoseNameAccount.YourName
    claim.CircumstancesPaymentChangeBankFullName = "HSBC"
    claim.CircumstancesPaymentChangeSortCode = "112233"
    claim.CircumstancesPaymentChangeAccountNumber = "12345678"
    claim.CircumstancesPaymentChangePaymentFrequency = PaymentFrequency.EveryWeek

    claim
  }

  def declaration = {
    val claim = otherChangeInfo
    claim.FurtherInfoContact = "012347688"
    claim.CircumstancesDeclarationInfoAgreement = "yes"
    claim.CircumstancesDeclarationWhy = "Cause I want"
    claim.CircumstancesDeclarationConfirmation = "yes"
    claim.CircumstancesSomeOneElseConfirmation = "yes"
    claim.NameOrOrganisation = "Mr Smith"
    claim
  }
}
