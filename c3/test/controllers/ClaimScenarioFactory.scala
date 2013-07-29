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
    claim.AboutYourPartnerDoesYourPartnerLiveAtTheSameAddressAsYou = "No"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"
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
    // Their Contact Details
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street\nLine 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvideDaytimePhoneNumberPersonYouCare = "07922 222 222"
    // More About The Person
    claim.AboutTheCareYouProvideWhatTheirRelationshipToYou = "Father"
    claim.AboutTheCareYouProvideDoesPersonGetArmedForcesIndependencePayment = "No"
    claim.AboutTheCareYouProvideHasAnyoneelseClaimedCarerAllowance = "Yes"
    // Previous Carer Personal Details
    claim.AboutTheCareYouProvideFirstNamePreviousCarer = "Peter"
    claim.AboutTheCareYouProvideMiddleNamePreviousCarer = "Jackson"
    claim.AboutTheCareYouProvideSurnamePreviousCarer = "Benson"
    claim.AboutTheCareYouProvideNINOPreviousCarer = "BB123456B"
    claim.AboutTheCareYouProvideDateofBirthPreviousCarer = "02/06/1985"
    // Previous Carer Contact Details
    claim.AboutTheCareYouProvideAddressPreviousCarer = "123 Conway Road\n Preston"
    claim.AboutTheCareYouProvidePostcodePreviousCarer = "BB9 1AB"
    claim.AboutTheCareYouProvideDaytimePhoneNumberPreviousCarer = "02933 333 333"
    claim.AboutTheCareYouProvideMobileNumberPreviousCarer = "07933 333 333"
    // Representatives For The Person
    claim.AboutTheCareYouProvideDoYouActforthePersonYouCareFor = "Yes"
    claim.AboutTheCareYouProvideYouActAs = "guardian"
    claim.AboutTheCareYouProvideDoesSomeoneElseActForThePersonYouCareFor = "Yes"
    claim.AboutTheCareYouProvidePersonActsAs = "guardian"
    claim.AboutTheCareYouProvideFullNameRepresentativesPersonYouCareFor = "Mary Jane Watson"
    // More About The Care
    claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "No"
    claim.AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours = "Yes"
    claim.AboutTheCareYouProvideWhenDidYouStarttoCareForThisPerson = "03/04/2013"
    claim.AboutTheCareYouProvideHasSomeonePaidYoutoCare = "Yes"
    // One Who Pays Personal Details
    claim.AboutTheCareYouProvideOrganisationPaysYou = "Valtech"
    claim.AboutTheCareYouProvideTitlePersonPaysYou = "Mr"
    claim.AboutTheCareYouProvideFirstNamePersonPaysYou = "Brian"
    claim.AboutTheCareYouProvideMiddleNamePersonCareFor = "Green"
    claim.AboutTheCareYouProvideSurnamePersonPaysYou = "Eldred"
    claim.AboutTheCareYouProvideHowMuchDoYouGetPaidAWeek = "£120"
    claim.AboutTheCareYouProvideWhenDidThePaymentsStart = "29/04/2013"
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
    // About self employment
    claim.SelfEmployedAreYouSelfEmployedNow = "no"
    // G6 Childcare provider's contact Details
    claim.SelfEmployedChildcareProviderAddress = "Care Provider Address"
    claim.SelfEmployedChildcareProviderPostcode = "SE1 6EH"
    // G7 Expenses while at work
    claim.SelfEmployedCareExpensesNameOfPerson = "Expenses Name Of Person"
    // G8 Care provider's contact Details
    claim.SelfEmployedCareProviderAddress = "Care Provider Address"
    claim.SelfEmployedCareProviderPostcode = "SE1 6EH"
    // G9 Completion
    //   None

    claim
  }

  def s9SelfEmploymentYourAccounts = {
    val claim = s9SelfEmployment
    //About self employment
    claim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis = "yes"
    claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "no"
    claim.SelfEmployedTellUsWhyandWhentheChangeHappened = "A Year back"
    claim.SelfEmployedDoYouHaveAnAccountant = "yes"
    claim.SelfEmployedCanWeContactYourAccountant = "yes"

    claim
  }

  def s9SelfEmploymentAccountantContactDetails = {
    val claim = s9SelfEmploymentYourAccounts
    //About self employment
    claim.SelfEmployedAccountantName = "Hello 123"
    claim.SelfEmployedAccountantAddress = "lineOne lineTwo lineThree"

    claim
  }

  def s9SelfEmploymentPensionsAndExpenses = {
    val claim = s9SelfEmploymentAccountantContactDetails
    //About self employment
    claim.SelfEmployedDoYouPayTowardsPensionScheme = "yes"
    claim.SelfEmployedHowMuchPayPensionExpenses = "11"
    claim.SelfEmployedDoYouPayAnyonetoLookAfterYourChild = "yes"
    claim.SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor = "yes"

    claim
  }


}
