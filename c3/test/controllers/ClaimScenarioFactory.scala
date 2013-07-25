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

  def s2ands3WithTimeOUtsideUKAndProperty() = {
    val claim = s2AboutYouWithTimeOutside()
    // Partner personal details
    claim.AboutYourPartnerTitle = "Mrs"
    claim.AboutYourPartnerFirstName = "Cloe"
    claim.AboutYourPartnerMiddleName = "Scott"
    claim.AboutYourPartnerSurname = "Smith"
    claim.AboutYourPartnerOtherNames = "Doe"
    claim.AboutYourPartnerNINO = "AB123456A"
    claim.AboutYourPartnerDateofBirth = "12/07/1990"
    claim.AboutYourPartnerNationality = "British"
    // More about your partner
    claim.AboutYourPartnerDoesYourPartnerLiveAtTheSameAddressAsYou = "Yes"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
    claim
  }

  def s4CareYouProvide() = {
    val claim = s2ands3WithTimeOUtsideUKAndProperty()
    // Their Personal Details
    claim.AboutTheCareYouProvideTitlePersonCareFor = "Mr"
    claim.AboutTheCareYouProvideFirstNamePersonCareFor = "Tom"
    claim.AboutTheCareYouProvideMiddleNamePersonCareFor = "Potter"
    claim.AboutTheCareYouProvideSurnamePersonCareFor = "Wilson"
    claim.AboutTheCareYouProvideNINOPersonCareFor = "AA123456A"
    claim.AboutTheCareYouProvideDateofBirthPersonYouCareFor = "02/03/1990"
    claim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "Yes"
    //  Their Contact Details
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street\nLine 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvideDaytimePhoneNumberPersonYouCare = "07922 222 222"
    claim
  }


  def s6PayDetails() = {
    val claim = new ClaimScenario
    claim.HowWePayYouHowWouldYouLikeToGetPaid =  "You don't have an account but intend to open one"
    claim.HowWePayYouHowOftenDoYouWantToGetPaid = "Every week"
    claim
  }


  def s8otherMoney = {
    val claim = s2AboutYouWithTimeOutside()
    //About other money
    claim.OtherMoneyHaveYouClaimedOtherBenefits = "no"
    //Money paid to someone welse for you
    claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "no"
    //Person Who Gets This Money
    claim.OtherMoneyOtherPersonFullName = "Jason"
    claim.OtherMoneyOtherPersonBenefit = "Benefit Name"
    // G4 Person Contact Details
    claim.OtherMoneySMPEmployerName = "Employers Name"
    claim.OtherMoneyOtherPersonAddress = "Other Person Address"
    claim.OtherMoneyOtherPersonPostcode = "SE1 6EH"
    // G5 Statutory Sick Pay
    claim.OtherMoneyHaveYouSSPSinceClaim = "no"
    // G6 Other Statutory Pay
    claim.OtherMoneyHaveYouSMPSinceClaim = "no"

    claim
  }
  
  
  def s9SelfEmployment = {
    val claim = s8otherMoney
    //About self employment
    claim.SelfEmployedAreYouSelfEmployedNow = "no"
      
    // Completion
    //   None

    claim
  }
}
