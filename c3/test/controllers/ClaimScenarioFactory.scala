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
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2016"
    claim.ClaimDateDidYouCareForThisPersonfor35Hours = "No"

    claim
  }

  def s12ClaimDateSpent35HoursYes() = {
    val claim = new TestData
    // Claim date
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2016"
    claim.ClaimDateDidYouCareForThisPersonfor35Hours = "Yes"
    claim.ClaimDateWhenDidYouStartToCareForThisPerson = "03/04/2013"

    claim
  }

  def s12ClaimDateInFuture() = {
    val claim = new TestData
    claim.ClaimDateDidYouCareForThisPersonfor35Hours = "No"
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/01/2099"

    claim
  }

  def s12ClaimDateInPast() = {
    val claim = new TestData
    claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/01/2015"

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
    claim.AboutYouWantsEmailContact = "No"
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"

    claim
  }

  def yourNationalityAndResidencyResident() = {
    val claim = new TestData
    claim.AboutYouNationalityAndResidencyNationality = "British"
    claim.AboutYouNationalityAndResidencyResideInUK = "Yes"
    claim
  }

  def maritalStatus() = {
    val claim = new TestData
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"

    claim
  }

  def yourNationalityAndResidencyNonResident() = {
    val claim = new TestData
    claim.AboutYouNationalityAndResidencyNationality = "Another nationality"
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
    claim.OtherMoneyOtherEEAGuardQuestion = "yes"
    claim.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEA = "yes"
    claim.OtherMoneyOtherAreYouReceivingPensionFromAnotherEEADetails = "I have a pension in Spain."
    claim.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEA = "yes"
    claim.OtherMoneyOtherAreYouPayingInsuranceToAnotherEEADetails = "details details details details "

    claim
  }

  def s2AboutYouWithTimeOutside() = {
    // Your details + outside UK
    val claim = yourDetailsEnablingTimeOutsideUK()

    // Status
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"

    // Your contact details
    claim.AboutYouAddress = "An address&Preston"
    claim.AboutYouPostcode = "SE1 6EH"
    claim.HowWeContactYou = "01253111111"
    claim.AboutYouWantsEmailContact = "No"
    // Claim date
    //claim.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "03/05/2014"
    // Nationality and Residency
    claim.AboutYouNationalityAndResidencyNationality = "British"
    // Abroad For More Than 52 Weeks
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "no"

    // Other EEA State or Switzerland
    claim.OtherMoneyOtherEEAGuardQuestion = "yes"
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
    claim.AboutYouTitle = "Mrs"
    claim.AboutYouFirstName = "Jane"
    claim.AboutYouSurname = "Doe"
    claim.AboutYouNationalInsuranceNumber = "AB123456D"
    claim.AboutYouDateOfBirth = "12/07/1970"

    // Your contact details
    claim.AboutYouAddress = "An address&Preston"
    claim.AboutYouPostcode = "SE1 6EH"
    claim.HowWeContactYou = "07111111111"
    claim.AboutYouContactYouByTextphone = "No"
    claim.AboutYouMobileNumber = "07111111111"

    // Nationality and Residency
    claim.AboutYouNationalityAndResidencyNationality = "British"
    // Abroad For More Than 52 Weeks
    claim.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "no"
    // Other EEA State or Switzerland
    claim.OtherMoneyOtherEEAGuardQuestion = "yes"
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
    claim.AboutYourPartnerNationality = "British"
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
    claim.AboutYourPartnerNationality = "British"
    claim.AboutYourPartnerHaveYouSeparatedfromYourPartner = "Yes"
    // Person you care for
    claim.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"
    claim
  }

  def s4CareYouProvide(hours35: Boolean, liveSameAddress: Boolean = false) = {
    val claim = s2ands3WithTimeOUtsideUKAndProperty()
    // Their Personal Details
    claim.AboutTheCareYouProvideTitlePersonCareFor = "Mr"
    claim.AboutTheCareYouProvideFirstNamePersonCareFor = "Tom"
    claim.AboutTheCareYouProvideMiddleNamePersonCareFor = "Potter"
    claim.AboutTheCareYouProvideSurnamePersonCareFor = "Wilson"
    claim.AboutTheCareYouProvideNINOPersonCareFor = "AA123456A"
    claim.AboutTheCareYouProvideDateofBirthPersonYouCareFor = "02/03/1990"
    if (liveSameAddress) {
      claim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "yes"

    } else {
      claim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "no"

    }
    // Their Contact Details
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street&Line 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvidePhoneNumberPersonYouCare = "07922 222 222"
    // More About The Person
    claim.AboutTheCareYouProvideWhatTheirRelationshipToYou = "Father"
    claim.AboutTheCareYouProvideHasAnyoneelseClaimedCarerAllowance = "Yes"
    // Previous Carer Personal Details
    claim.AboutTheCareYouProvideFirstNamePreviousCarer = "Peter"
    claim.AboutTheCareYouProvideMiddleNamePreviousCarer = "Jackson"
    claim.AboutTheCareYouProvideSurnamePreviousCarer = "Benson"
    claim.AboutTheCareYouProvideNINOPreviousCarer = "BB123456B"
    claim.AboutTheCareYouProvideDateofBirthPreviousCarer = "02/06/1985"
    // Previous Carer Contact Details
    claim.AboutTheCareYouProvideAddressPreviousCarer = "123 Conway Road&Preston"
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
      claim.ClaimDateDidYouCareForThisPersonfor35Hours = "Yes"
      claim.ClaimDateWhenDidYouStartToCareForThisPerson = "03/04/2013"
    } else {
      claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "No"
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
    claim.AboutTheCareYouProvideAddressPersonPaysYou = "123 Cleverme Street&Genius"
    claim.AboutTheCareYouProvidePostcodePersonPaysYou = "GN1 2DA"
    claim
  }

  def s4CareYouProvideWithNoPersonalDetails() = {
    val claim = new TestData
    // Their Personal Details
    claim.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "no"
    // Their Contact Details
    claim.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street&Line 2"
    claim.AboutTheCareYouProvidePostcodePersonCareFor = "BB9 2AD"
    claim.AboutTheCareYouProvidePhoneNumberPersonYouCare = "07922 222 222"
    // More About The Person
    claim.AboutTheCareYouProvideWhatTheirRelationshipToYou = "Father"
    claim.AboutTheCareYouProvideHasAnyoneelseClaimedCarerAllowance = "Yes"
    // More About The Care
    claim.AboutTheCareYouProvideDoYouSpend35HoursorMoreEachWeek = "Yes"
    claim.AboutTheCareYouProvideHasSomeonePaidYoutoCare = "Yes"

    // Breaks in care
    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_1 = "At home"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_1 = "In hospital"
    claim.AboutTheCareYouProvideBreakStartDate_1 = "12/12/2006"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_1 = "no"
    claim.AboutTheCareYouProvideHasBreakEnded_1 = "No"

    claim
  }

  def s4CareYouProvideWithNoBreaksInCare() = {
    val claim = s4CareYouProvide(true)

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

    claim
  }

  def s4CareYouProvideWithBreaksInCare(hours35: Boolean) = {
    val claim = if (hours35) s4CareYouProvide(true) else s4CareYouProvide(false)

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_1 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_1 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_1 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_1 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_1 = "No"

    claim
  }

  def s4CareYouProvideWithMultipleBreaksInCare(hours35: Boolean) = {
    val claim = if (hours35) s4CareYouProvide(true) else s4CareYouProvide(false)

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_1 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_1 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_1 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_1 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_1 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_2 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_2 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_2 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_2 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_2 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_2 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_3 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_3 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_3 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_3 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_3 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_3 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_4 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_4 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_4 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_4 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_4 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_4 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_5 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_5 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_5 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_5 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_5 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_5 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_6 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_6 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_6 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_6 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_6 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_6 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_7 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_7 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_7 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_7 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_7 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_7 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_8 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_8 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_8 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_8 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_8 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_8 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_9 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_9 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_9 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_9 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_9 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_9 = "No"

    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_10 = "yes"
    claim.AboutTheCareYouProvideBreakStartDate_10 = "10/01/1999"
    claim.AboutTheCareYouProvideWhereWereYouDuringTheBreak_10 = "In hospital"
    claim.AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_10 = "In hospital"
    claim.AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_10 = "Yes"
    claim.AboutTheCareYouProvideHasBreakEnded_10 = "No"

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
    claim.HowWePayYouHowWouldYouLikeToGetPaid = "no"
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

  def s7SelfEmployedAndEmployed() = {
    val claim = s12ClaimDate()

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "Yes"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    claim
  }

  def s7SelfEmployedNotEmployed() = {
    val claim = s12ClaimDate()

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "Yes"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
    claim
  }

  def s7EmployedNotSelfEmployed() = {
    val claim = s12ClaimDate()

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    claim
  }

  def s7NotEmployedNorSelfEmployed() = {
    val claim = s12ClaimDate()

    // Employment
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
    claim
  }

  def s7MandatoryJobDetails() = {
    val claim = new TestData

    claim.EmploymentEmployerName_1 = "Toys r not Us"
    claim.EmploymentEmployerPhoneNumber_1 = "12345678"
    claim.EmploymentEmployerAddress_1 = "Street Test 1&Street Test 2"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_1 = "Yes"
    claim.EmploymentHaveYouFinishedThisJob_1 = "No"

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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "yes"
    claim.EmploymentPayForThings_1 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s7EmploymentMinimum(hours35: Boolean) = {
    val claim = if (hours35) s4CareYouProvide(true) else s4CareYouProvide(false)
    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"
    claim.EducationHaveYouBeenOnACourseOfEducation = "no"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime = "yes"
    claim.EmploymentDoYouWantToAddAnythingAboutYourWork = "no"

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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "yes"
    claim.EmploymentPayForThings_1 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "no"
    claim
  }

  def s7EmploymentMaximum(hours35: Boolean) = {
    val claim = if (hours35) s4CareYouProvide(true) else s4CareYouProvide(false)
    claim.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"
    claim.EducationHaveYouBeenOnACourseOfEducation = "no"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "Yes"
    claim.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "No"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime = "yes"
    claim.EmploymentDoYouWantToAddAnythingAboutYourWork = "no"

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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "yes"
    claim.EmploymentPayForThings_1 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "yes"

    claim.EmploymentEmployerName_2 = "Tesco's 2"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_2 = "no"
    claim.EmploymentWhenDidYouStartYourJob_2 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_2 = "yes"
    claim.EmploymentWhenDidYouLastWork_2 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_2 = "25"
    claim.EmploymentEmployerAddress_2 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_2 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_2 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_2 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_2 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_2 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_2 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_2 = "Other"
    claim.EmploymentAddtionalWageOther_2 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_2 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_2 = "no"
    claim.EmploymentWhatPeriodIsItForFrom_2 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_2 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_2 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_2 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_2 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_2 = "no"
    claim.EmploymentDoYouPayForThingsToDoJob_2 = "yes"
    claim.EmploymentPayForThings_2 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_2 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_2 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_2 = "yes"

    claim.EmploymentEmployerName_3 = "Tesco's 3"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_3 = "no"
    claim.EmploymentWhenDidYouStartYourJob_3 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_3 = "yes"
    claim.EmploymentWhenDidYouLastWork_3 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_3 = "25"
    claim.EmploymentEmployerAddress_3 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_3 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_3 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_3 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_3 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_3 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_3 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_3 = "Other"
    claim.EmploymentAddtionalWageOther_3 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_3 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_3 = "no"
    claim.EmploymentWhatPeriodIsItForFrom_3 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_3 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_3 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_3 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_3 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_3 = "no"
    claim.EmploymentDoYouPayForThingsToDoJob_3 = "yes"
    claim.EmploymentPayForThings_3 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_3 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_3 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_3 = "yes"

    claim.EmploymentEmployerName_4 = "Tesco's 4"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_4 = "no"
    claim.EmploymentWhenDidYouStartYourJob_4 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_4 = "yes"
    claim.EmploymentWhenDidYouLastWork_4 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_4 = "25"
    claim.EmploymentEmployerAddress_4 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_4 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_4 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_4 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_4 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_4 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_4 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_4 = "Other"
    claim.EmploymentAddtionalWageOther_4 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_4 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_4 = "no"
    claim.EmploymentWhatPeriodIsItForFrom_4 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_4 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_4 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_4 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_4 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_4 = "no"
    claim.EmploymentDoYouPayForThingsToDoJob_4 = "yes"
    claim.EmploymentPayForThings_4 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_4 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_4 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_4 = "yes"

    claim.EmploymentEmployerName_5 = "Tesco's 5"
    claim.EmploymentDidYouStartThisJobBeforeClaimDate_5 = "no"
    claim.EmploymentWhenDidYouStartYourJob_5 = "01/01/2013"
    claim.EmploymentHaveYouFinishedThisJob_5 = "yes"
    claim.EmploymentWhenDidYouLastWork_5 = "01/07/2013"
    claim.EmploymentHowManyHoursAWeekYouNormallyWork_5 = "25"
    claim.EmploymentEmployerAddress_5 = "23 Yeadon Way&Blackpool&Lancashire"
    claim.EmploymentEmployerPostcode_5 = "FY4 5TH"
    claim.EmploymentEmployerPhoneNumber_5 = "01253 667889"
    claim.EmploymentWhenWereYouLastPaid_5 = "08/07/2013"
    claim.EmploymentWhatWasTheGrossPayForTheLastPayPeriod_5 = "600"
    claim.EmploymentWhatWasIncludedInYourLastPay_5 = "All amounts due"
    claim.EmploymentDoYouGettheSameAmountEachTime_5 = "no"
    claim.EmploymentAddtionalWageHowOftenAreYouPaid_5 = "Other"
    claim.EmploymentAddtionalWageOther_5 = "Quarterly"
    claim.EmploymentAddtionalWageWhenDoYouGetPaid_5 = "two weeks ago"
    claim.EmploymentAdditionalWageDoesYourEmployerOweYouAnyMoney_5 = "no"
    claim.EmploymentWhatPeriodIsItForFrom_5 = "03/04/2013"
    claim.EmploymentWhatPeriodIsItForTo_5 = "03/05/2013"
    claim.EmploymentWhatIsTheMoneyOwedFor_5 = "This and that"
    claim.EmploymentWhenShouldTheMoneyOwedHaveBeenPaid_5 = "06/05/2013"
    claim.EmploymentWhenWillYouGetMoneyOwed_5 = "08/08/2013"
    claim.EmploymentDoYouPayForPensionExpenses_5 = "no"
    claim.EmploymentDoYouPayForThingsToDoJob_5 = "yes"
    claim.EmploymentPayForThings_5 = "some expenses to do the job"
    claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_5 = "yes"
    claim.EmploymentWhatAreNecessaryJobExpenses_5 = "some job expenses in the amount of 200 to xyz"
    claim.EmploymentHaveYouBeenEmployedAtAnyTime_5 = "no"
    
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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "no"
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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "yes"
    claim.EmploymentPayForThings_1 = "some expenses to do the job"
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
    claim.EmploymentDoYouPayForThingsToDoJob_1 = "yes"
    claim.EmploymentPayForThings_1 = "some expenses to do the job"
    claim.EmploymentPensionExpenses_1 = "some pension expenses in the amount of 200 to xyz"
    claim.EmploymentWhatAreNecessaryJobExpenses_1 = "some job expenses in the amount of 200 to xyz"

    claim
  }

  def s9OtherIncome = {
    val claim = s7Employment()
    claim.PaymentTypesForThisPay = "MaternityOrPaternityPay"
    claim.StillBeingPaidThisPay = "yes"
    claim.WhoPaidYouThisPay = "The Man"
    claim.AmountOfThisPay = "12"
    claim.HowOftenPaidThisPay = "Monthly"

    claim
  }

  def s9OtherIncomeOther = {
    val claim = s7Employment()
    claim.PaymentTypesForThisPay = "MaternityOrPaternityPay"
    claim.StillBeingPaidThisPay = "yes"
    claim.WhoPaidYouThisPay = "The Man"
    claim.AmountOfThisPay = "12"
    claim.HowOftenPaidThisPay = "Other"
    claim.HowOftenPaidThisPayOther = "every day and twice on Sundays"

    claim
  }

  def s9SelfEmployment = {
    val claim = s9OtherIncome
    // About self-employment
    claim.SelfEmployedAreYouSelfEmployedNow = "no"
    claim.SelfEmployedMoreThanYearAgo = "no"
    claim.SelfEmployedDoYouKnowYourTradingYear = "no"
    claim.SelfEmployedWhenDidYouStartThisJob = "11/09/2001"
    claim.SelfEmployedWhenDidTheJobFinish = "07/07/2005"
    claim.SelfEmployedPaidMoneyYet = "no"

    // G Pension and Expenses
    claim.SelfEmploymentDoYouPayForPensionExpenses = "Yes"
    claim.SelfEmploymentPensionExpenses = "Some self-employment pension expenses"
    claim.SelfEmploymentDoYouPayForAnythingNecessaryToDoYourJob = "Yes"
    claim.SelfEmploymentWhatAreNecessaryJobExpenses = "Some self-employment job expenses"

    claim
  }

  def s9SelfEmploymentYourAccounts = {
    val claim = s9SelfEmployment
    //About self-employment
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

  def s7EmploymentAdditionalInfo = {
    val claim = new TestData
    claim.EmploymentDoYouWantToAddAnythingAboutYourWork = "yes"
    claim.EmploymentAdditionalInfo = "I do not have more information"
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

  def previewAboutYouForBankDetails = {
    val claim = s12ClaimDate()
    claim.AboutYouTitle = "Mr"
    claim.AboutYouFirstName = "John"
    claim.AboutYouSurname = "Appleseed"
    claim.AboutYouNationalityAndResidencyNationality = "British"
    claim.AboutYouNationalityAndResidencyResideInUK = "Yes"
    claim.AboutYouNINO = "AB123456C"
    claim.AboutYouAddress = "101 Clifton Street&Blackpool"
    claim.AboutYouPostcode = "FY1 2RW"
    claim.HowWeContactYou = "01772 888901"
    claim.AboutYouWantsEmailContact = "No"
    claim.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = "Single"
    claim.AboutYouDateOfBirth = "02/02/1950"

    claim
  }

  def previewLessThan65WithBankDetails = {
    val claim = previewAboutYouForBankDetails
    claim.AboutYouDateOfBirth = "02/02/1980"
    claim.HowWePayYouHowWouldYouLikeToGetPaid = "yes"
    claim.HowWePayYouHowOftenDoYouWantToGetPaid = PaymentFrequency.EveryWeek
    claim.HowWePayYouNameOfAccountHolder = "John Smith"
    claim.WhoseNameOrNamesIsTheAccountIn = "John Smith"
    claim.HowWePayYouFullNameOfBankorBuildingSociety = "Carers Bank"
    claim.HowWePayYouSortCode = "090126"
    claim.HowWePayYouAccountNumber = "12345678"

    claim
  }

  def previewLessThan65WithNoBankDetails = {
    val claim = previewAboutYouForBankDetails
    claim.AboutYouDateOfBirth = "02/02/1980"
    claim.HowWePayYouHowWouldYouLikeToGetPaid = "no"
    claim.HowWePayYouHowOftenDoYouWantToGetPaid = PaymentFrequency.EveryWeek

    claim
  }

  def thirdPartyYesCarer() = {
    val claim = new TestData
    claim.ThirdPartyAreYouApplying = "yesCarer"
    claim
  }

  def thirdPartyNotCarer() = {
    val claim = new TestData
    claim.ThirdPartyAreYouApplying = "noCarer"
    claim.ThirdPartyNameAndOrganisation = "test and company"
    claim
  }

  def feedbackSatisfiedVS() = {
    val claim = new TestData
    claim.FeedbackSatisfied = "VS"
    claim
  }
}
