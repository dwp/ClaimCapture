package controllers

import utils.pageobjects.TestData

import app.{PensionPaymentFrequency, WhoseNameAccount, PaymentFrequency, AccountStatus}

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
object ClaimScenarioFactory {

  val partnerAddress = "Partner Address"
  val partnerPostcode = "RM11 1AA"

  def s12ClaimDate() = {
    val claim = new TestData
    // Claim date
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2014"
    claim
  }

  def yourDetailsWithNotTimeOutside() = {
    val claim = s12ClaimDate()
    claim.AboutYouTitle = "Mr"
    claim.AboutYouFirstName = "John"
    claim.AboutYouSurname = "Appleseed"
    claim.AboutYouDateOfBirth = "03/04/1950"
    claim.AboutYouNationalityAndResidencyNationality = "British"
    claim.AboutYouNationalityAndResidencyResideInUK = "Yes"
    claim.AboutYouNINO = "AB123456C"
    claim.AboutYouAddress = "101 Clifton Street&Blackpool"
    claim.AboutYouPostcode = "FY1 2RW"
    claim.HowWeContactYou = "01772 888901"
    claim
  }

  def yourNationalityAndResidencyResident() = {
    val claim = new TestData
    claim.AboutYouNationalityAndResidencyNationality = "British"
    claim.AboutYouNationalityAndResidencyResideInUK = "Yes"
    claim
  }

  def yourNationalityAndResidencyNonResident() = {
    val claim = new TestData
    claim.AboutYouNationalityAndResidencyNationality = "Another Country"
    claim.AboutYouNationalityAndResidencyActualNationality = "French"
    claim.AboutYouNationalityAndResidencyResideInUK = "No"
    claim.AboutYouNationalityAndResidencyNormalResidency = "France"
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"
    claim
  }

  def abroadForMoreThan52WeeksConfirmationYes() = {
    val claim = new TestData
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "Yes"
    claim.AboutYouTripDetails_1 = "Trip 1 to London"

    claim
  }

  def abroadForMoreThan52WeeksConfirmationNo() = {
    val claim = new TestData
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "No"

    claim
  }

  def abroadForMoreThan52WeeksTrip1() = {
    val claim = abroadForMoreThan52WeeksConfirmationYes()

    // Trip
    claim.DateYouLeftGB_1 = "10/04/2013"
    claim.DateYouReturnedToGB_1 = "20/04/2013"
    claim.WhereDidYouGo_1 = "France"
    claim.WhyDidYou_1 = "Holiday"
    claim.PersonWithYou_1 = "yes"
    claim
  }

  def abroadForMoreThan52WeeksTrip2() = {
    val claim = new TestData
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_2 = "Yes"
    // Trip
    claim.DateYouLeftGB_2 = "10/05/2013"
    claim.DateYouReturnedToGB_2 = "20/05/2013"
    claim.WhereDidYouGo_2 = "Spain"
    claim.WhyDidYou_2 = "Holiday"
    claim.PersonWithYou_2 = "no"
    claim
  }

  def yourDetailsEnablingTimeOutsideUK() = {
    val claim = yourDetailsWithNotTimeOutside()

    claim
  }

  def otherEuropeanEconomicArea() = {
    val claim = new TestData

    // G7 EEA state or Switzerland
    claim.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA = "yes"
    claim.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "yes"

    claim
  }

  def s2AboutYouWithTimeOutside() = {
    // Your details + outside UK
    val claim = yourDetailsEnablingTimeOutsideUK()
    // Your contact details
    claim.AboutYouAddress = "An address"
    claim.AboutYouPostcode = "SE1 6EH"
    claim.HowWeContactYou = "01253 111 111"
    // Claim date
    //claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "03/05/2014"
    // Nationality and Residency
    claim.AboutYouNationalityAndResidencyNationality = "British"
    // Abroad For More Than 52 Weeks
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "no"

    // Other EEA State or Switzerland
    claim.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA = "no"
    claim.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "no"

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "Yes"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    claim
  }

  def s2AnsweringNoToQuestions() = {
    val claim = new TestData
    //Your details
    claim.AboutYouTitle="Mrs"
    claim.AboutYouFirstName="Jane"
    claim.AboutYouSurname="Doe"
    claim.AboutYouNationalInsuranceNumber="AB123456D"
    claim.AboutYouDateOfBirth = "12/07/1970"

    // Your contact details
    claim.AboutYouAddress = "An address"
    claim.AboutYouPostcode = "SE1 6EH"
    claim.HowWeContactYou = "07111 111 111"
    claim.AboutYouContactYouByTextphone = "No"
    claim.AboutYouMobileNumber = "07111 111 111"

    // Nationality and Residency
    claim.AboutYouNationalityAndResidencyNationality = "British"
    // Abroad For More Than 52 Weeks
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "no"
    // Other EEA State or Switzerland
    claim.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA = "no"
    claim.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "no"

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
    claim.AboutYourPartnerHaveYouSeparatedfromYourPartner = "Yes"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
    claim.AboutYourPartnerHadPartnerSinceClaimDate = "Yes"
    claim
  }

  def s3YourPartnerNotThePersonYouCareFor() = {
    val claim = s2AboutYouWithTimeOutside()
    // Partner personal details
    claim.AboutYourPartnerHadPartnerSinceClaimDate = "Yes"
    claim.AboutYourPartnerTitle = "Mrs"
    claim.AboutYourPartnerFirstName = "Cloe"
    claim.AboutYourPartnerMiddleName = "Scott"
    claim.AboutYourPartnerSurname = "Smith"
    claim.AboutYourPartnerOtherNames = "Doe"
    claim.AboutYourPartnerNINO = "AB123456A"
    claim.AboutYourPartnerDateofBirth = "12/07/1990"
    claim.AboutYourPartnerNationality = "British"
    claim.AboutYourPartnerHaveYouSeparatedfromYourPartner = "Yes"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
    claim
  }

  def s3YourPartnerNotThePersonYouCareForWithBritishNationality() = {
    val claim = s2AboutYouWithTimeOutside()
    // Partner personal details
    claim.AboutYourPartnerHadPartnerSinceClaimDate = "Yes"
    claim.AboutYourPartnerTitle = "Mrs"
    claim.AboutYourPartnerFirstName = "Cloe"
    claim.AboutYourPartnerMiddleName = "Scott"
    claim.AboutYourPartnerSurname = "Smith"
    claim.AboutYourPartnerOtherNames = "Doe"
    claim.AboutYourPartnerNINO = "AB123456A"
    claim.AboutYourPartnerDateofBirth = "12/07/1990"
    claim.AboutYourPartnerHaveYouSeparatedfromYourPartner = "Yes"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
    claim
  }

  def s4CareYouProvide(hours35:Boolean) = {
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
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street&Line 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvidePhoneNumberPersonYouCare = "07922 222 222"
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
    claim.AboutTheCareYouProvideAddressPreviousCarer = "123 Conway Road& Preston"
    claim.AboutTheCareYouProvidePostcodePreviousCarer = "BB9 1AB"
    claim.AboutTheCareYouProvidePhoneNumberPreviousCarer = "02933 333 333"
    claim.AboutTheCareYouProvideMobileNumberPreviousCarer = "07933 333 333"
    // Representatives For The Person
    claim.AboutTheCareYouProvideDoYouActforthePersonYouCareFor = "Yes"
    claim.AboutTheCareYouProvideYouActAs = "Guardian"
    claim.AboutTheCareYouProvideDoesSomeoneElseActForThePersonYouCareFor = "Yes"
    claim.AboutTheCareYouProvidePersonActsAs = "Guardian"
    claim.AboutTheCareYouProvideFullNameRepresentativesPersonYouCareFor = "Mary Jane Watson"
    // More About The Care
    if (hours35) {
      claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "Yes"
      claim.AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours = "Yes"
      claim.AboutTheCareYouProvideWhenDidYouStarttoCareForThisPerson = "03/04/2013"
    }else {
      claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "No"
      claim.AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours = "No"
    }


    claim.AboutTheCareYouProvideHasSomeonePaidYoutoCare = "Yes"
    // One Who Pays Personal Details
    claim.AboutTheCareYouProvideOrganisationPaysYou = "Valtech"
    claim.AboutTheCareYouProvideTitlePersonPaysYou = "Mr"
    claim.AboutTheCareYouProvideFirstNamePersonPaysYou = "Brian"
    claim.AboutTheCareYouProvideMiddleNamePersonPaysYou = "Green"
    claim.AboutTheCareYouProvideSurnamePersonPaysYou = "Eldred"
    claim.AboutTheCareYouProvideHowMuchDoYouGetPaidAWeek = "£120"
    claim.AboutTheCareYouProvideWhenDidThePaymentsStart = "29/04/2013"
    // Contact Details Of Paying Person
    claim.AboutTheCareYouProvideAddressPersonPaysYou = "123 Cleverme Street & Genius"
    claim.AboutTheCareYouProvidePostcodePersonPaysYou = "GN1 2DA"
    claim
  }

  def s4CareYouProvideWithNoPersonalDetails() = {
    val claim = new TestData
    // Their Personal Details
    claim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "Yes"
    // Their Contact Details
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street&Line 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvidePhoneNumberPersonYouCare = "07922 222 222"
    // More About The Person
    claim.AboutTheCareYouProvideWhatTheirRelationshipToYou = "Father"
    claim.AboutTheCareYouProvideDoesPersonGetArmedForcesIndependencePayment = "No"
    claim.AboutTheCareYouProvideHasAnyoneelseClaimedCarerAllowance = "Yes"
    // More About The Care
    claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "Yes"
    claim.AboutTheCareYouProvideDidYouCareForThisPersonfor35Hours = "Yes"
    claim.AboutTheCareYouProvideWhenDidYouStarttoCareForThisPerson = "03/04/2013"
    claim.AboutTheCareYouProvideHasSomeonePaidYoutoCare = "Yes"

    // Breaks in care
    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_1 = "Home"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_1 = "Hospital"
    claim.AboutTheCareYouProvideBreakStartDate_1 = "12/12/2006"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_1 = "no"

    claim
  }

  def s4CareYouProvideWithNoBreaksInCare() = {
    val claim = s4CareYouProvide(true)

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

    claim
  }

  def s4CareYouProvideWithBreaksInCare() = {
    val claim = s4CareYouProvide(true)

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_1 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_1 = "Hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_1 = "Hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_1 = "Yes"

    claim
  }

  def s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed() = {
    val claim = s4CareYouProvideWithNoBreaksInCare()

    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"

    // Education
    claim.EducationHaveYouBeenOnACourseOfEducation = "No"

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"

    claim.updateDynamic("Employed.visible")("false")
    claim.updateDynamic("SelfEmployed.visible")("false")

    claim
  }


  def s5TimeSpentAbroad() = {
    val claim = s4CareYouProvide(true)
    // Normal Residence And Current Location
    claim.TimeSpentAbroadDoYouNormallyLiveintheUk = "No"
    claim.TimeSpentAbroadWhereDoYouNormallyLive = "Spain"
    claim.TimeSpentAbroadAreYouinGBNow = "Yes"
    // Details of time abroad with the person you care for
    claim.TimeSpentAbroadHaveYouBeenOutOfGBWithThePersonYouCareFor_1 = "Yes"
    // Abroad For More Than 52 Weeks
    claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "Yes"
    // Trip
    claim.TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_1 = "10/04/2013"
    claim.TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_1 = "20/04/2013"
    claim.TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_1 = "Everywhere"
    claim.TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_1 = "Visit Family"
    claim
  }

  def s6Education() = {
    val claim = s5TimeSpentAbroad()
    // Address of School College or University
    claim.EducationHaveYouBeenOnACourseOfEducation = "Yes"
    claim.EducationCourseTitle = "Course 101"
    claim.EducationNameofSchool = "Lancaster University"
    claim.EducationNameOfMainTeacherOrTutor = "Dr. Ray Charles"
    claim.EducationPhoneNumber = "123456789"
    claim.EducationWhenDidYouStartTheCourse = "10/04/2013"
    claim.EducationWhenDoYouExpectTheCourseToEnd = "10/04/2013"

    claim
  }

  def s6PayDetails() = {
    val claim = new TestData
    claim.HowWePayYouHowWouldYouLikeToGetPaid = AccountStatus.AppliedForAccount
    claim.HowWePayYouHowOftenDoYouWantToGetPaid = PaymentFrequency.EveryWeek
    claim
  }

  def s6BankBuildingSocietyDetails() = {
    val claim = new TestData

    claim.HowWePayYouNameOfAccountHolder = "John Smith"
    claim.WhoseNameOrNamesIsTheAccountIn = WhoseNameAccount.YourName
    claim.HowWePayYouFullNameOfBankorBuildingSociety = "Carers Bank"
    claim.HowWePayYouSortCode = "090126"
    claim.HowWePayYouAccountNumber = "12345678"
    claim
  }


  def s7EmploymentMinimal() = {
    val claim = new TestData
    claim.EmploymentEmployerName_1 = "Tesco's"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_1 = "no"
    claim.EmploymentWhenDidYouStartYourJob_1 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_1 = "yes"
    claim.EmploymentWhenDidYouLastWork_1 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_1 = "25"
    claim.EmploymentEmployerAddress_1 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_1 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_1 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_1 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_1 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_1 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_1 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_1 = "Other"
    claim.EmploymentAddtionalWageOther_1 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_1 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_1 = "no"
    claim.EmploymentWhatPeriodIsItForFrom_1 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_1 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_1 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_1 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_1 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_1 = "no"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s7Employment() = {
    val claim = s2AboutYouWithTimeOutside
    claim.EmploymentEmployerName_1 = "Tesco's"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_1 = "no"
    claim.EmploymentWhenDidYouStartYourJob_1 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_1 = "yes"
    claim.EmploymentWhenDidYouLastWork_1 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_1 = "25"
    claim.EmploymentEmployerAddress_1 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_1 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_1 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_1 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_1 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_1 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_1 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_1 = "other"
    claim.EmploymentAddtionalWageOther_1 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_1 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_1 = "yes"
    claim.EmploymentHowMuchAreYouOwed_1 = "1250"
    claim.EmploymentWhatPeriodIsItForFrom_1 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_1 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_1 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_1 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_1 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_1 = "yes"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentPensionExpenses_1 = "some pension expenses in the amount of 200 to xyz"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s7EmploymentWhenFinishedJobNo() = {
    val claim = s2AboutYouWithTimeOutside
    claim.EmploymentEmployerName_1 = "Tesco's"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_1 = "no"
    claim.EmploymentWhenDidYouStartYourJob_1 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_1 = "no"
    claim.EmploymentEmployerAddress_1 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_1 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_1 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_1 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_1 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_1 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_1 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_1 = "other"
    claim.EmploymentAddtionalWageOther_1 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_1 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_1 = "yes"
    claim.EmploymentHowMuchAreYouOwed_1 = "1250"
    claim.EmploymentWhatPeriodIsItForFrom_1 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_1 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_1 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_1 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_1 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_1 = "yes"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentPensionExpenses_1 = "some pension expenses in the amount of 200 to xyz"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s7EmploymentBeforeClamDateYes() = {
    val claim = s2AboutYouWithTimeOutside
    claim.EmploymentEmployerName_1 = "Tesco's"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_1 = "yes"
    claim.EmploymentHaveYouFinishedThisJob_1 = "no"
    claim.EmploymentEmployerAddress_1 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_1 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_1 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_1 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_1 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_1 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_1 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_1 = "other"
    claim.EmploymentAddtionalWageOther_1 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_1 = "two weeks ago"
    claim.EmploymentHowMuchAreYouOwed_1 = "1250"
    claim.EmploymentWhatPeriodIsItForFrom_1 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_1 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_1 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_1 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_1 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_1 = "yes"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentPensionExpenses_1 = "some pension expenses in the amount of 200 to xyz"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s9otherMoney = {
    val claim = s7Employment()
    // G1 About other money
    claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
    claim.OtherMoneyWhoPaysYou = "The Man"
    claim.OtherMoneyHowMuch = "12"
    // G1 Statutory Sick Pay
    claim.OtherMoneyHaveYouSSPSinceClaim = "yes"
    claim.OtherMoneySSPHowMuch = "123"
    claim.OtherMoneySSPEmployerName = "Burger King"
    // G1 Other Statutory Pay
    claim.OtherMoneyHaveYouSMPSinceClaim = "yes"
    claim.OtherMOneySMPHowMuch = "123"
    claim.OtherMoneySMPEmployerName = "Employers Name"

    claim
  }
  
  def s9otherMoneyOther = {
    val claim = s7Employment()
    // G1 About other money
    claim.OtherMoneyAnyPaymentsSinceClaimDate = "yes"
    claim.OtherMoneyWhoPaysYou = "The Man"
    claim.OtherMoneyHowMuch = "12"
    claim.OtherMoneyHowOften = "Other"
    claim.OtherMoneyHowOftenOther = "every day and twice on Sundays"
    // G1 Statutory Sick Pay
    claim.OtherMoneyHaveYouSSPSinceClaim = "no"

    // G1 Other Statutory Pay
    claim.OtherMoneyHaveYouSMPSinceClaim = "no"


    claim
  }

  def s9SelfEmployment = {
    val claim = s9otherMoney
    // About self employment
    claim.SelfEmployedAreYouSelfEmployedNow = "no"
    claim.SelfEmployedWhenDidYouStartThisJob = "11/09/2001"
    claim.SelfEmployedWhenDidTheJobFinish = "07/07/2005"
    claim.SelfEmployedNatureofYourBusiness = "Some type of business"

    // G8 Pension and Expenses
    claim.SelfEmploymentDoYouPayForPensionExpenses = "Yes"
    claim.SelfEmploymentPensionExpenses = "Some self employment pension expenses"
    claim.SelfEmploymentDoYouPayForAnythingNecessaryToDoYourJob = "Yes"
    claim.SelfEmploymentWhatAreNecessaryJobExpenses = "Some self employment job expenses"

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

  def s9SelfEmploymentPensionsAndExpenses = {
    val claim = s9SelfEmploymentYourAccounts

    claim.SelfEmployedDoYouPayTowardsPensionScheme = "yes"
    claim.SelfEmployedHowMuchYouPayTowardsPensionScheme = "11.2"
    claim.SelfEmployedHowoftenYouPayTowardsPensionScheme = app.PensionPaymentFrequency.Other
    claim.SelfEmployedHowOftenOtherYouPayTowardsPensionScheme = "Every day and twice on Sunday's"
    claim.SelfEmployedDoYouPayAnyonetoLookAfterYourChild = "yes"
    claim.SelfEmployedDoYouPayAnyonetoLookAfterPersonYouCareFor = "yes"

    claim
  }

  def s9SelfEmploymentChildCareExpenses = {
    val claim = s9SelfEmploymentPensionsAndExpenses

    claim.SelfEmployedChildcareProviderNameOfPerson = "myself"
    claim.SelfEmployedChildcareExpensesHowMuchYouPay = "123.45"
    claim.SelfEmployedChildcareExpensesHowOften = app.PensionPaymentFrequency.Other
    claim.SelfEmployedChildcareExpensesHowOftenOther = "Every day and twice on Sunday's"
    claim.SelfEmployedChildcareProviderWhatRelationIsToYou = "Father"
    claim.SelfEmployedChildcareProviderWhatRelationIsToYourPartner = "Father"
    claim.SelfEmployedChildcareProviderWhatRelationIsTothePersonYouCareFor = "Father"

    claim
  }

  def s9SelfEmploymentExpensesRelatedToPersonYouCareFor = {
    val claim = s9SelfEmploymentChildCareExpenses

    claim.SelfEmployedCareExpensesHowMuchYouPay = "900.9"
    claim.SelfEmployedCareExpensesHowOften = app.PensionPaymentFrequency.Other
    claim.SelfEmployedCareExpensesHowOftenOther = "Every day and twice on Sunday's"
    claim.SelfEmployedCareExpensesNameOfPerson = "John"
    claim.SelfEmployedCareExpensesWhatRelationIsToYou = "Father"
    claim.SelfEmployedCareExpensesWhatRelationIsTothePersonYouCareFor = "Father"
    claim.SelfEmployedCareExpensesWhatRelationToPartner = "Father"

    claim
  }
  
  def s11ConsentAndDeclaration = {
    val claim = s9SelfEmployment
    claim.ConsentDeclarationTellUsAnythingElseAnswerAboutClaim = "no"
    claim.ConsentDeclarationCommunicationWelsh = "no"

    claim
  }

}
