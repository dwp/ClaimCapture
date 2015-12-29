package controllers

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit
import app.AccountStatus
import app.PaymentFrequency

object Formulate {
  val partnerAddress = "Partner Address"

  val partnerPostcode = "RM11 1AA"
    
  def yourDetails(browser: TestBrowser) = {
    browser.goTo("/about-you/your-details")
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_nino") `with` "AB123456C" // Pattern AB123456C
    browser.fill("#dateOfBirth_day") `with` "1"
    browser.fill("#dateOfBirth_month") `with` "1"
    browser.fill("#dateOfBirth_year") `with` "1950"
    //browser.fill("#nationality") `with` "English"
    //browser.click("#alwaysLivedUK_yes")
    browser.submit("button[type='submit']")
  }

  def yourDetailsEnablingTimeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/about-you/your-details")
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_nino") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.fill("#dateOfBirth_day") `with` "3"
    browser.fill("#dateOfBirth_month") `with` "4"
    browser.fill("#dateOfBirth_year") `with` "1950"
    //browser.fill("#nationality") `with` "English"
    //browser.click("#alwaysLivedUK_no")
    browser.submit("button[type='submit']")
  }

  def yourContactDetails(browser: TestBrowser) = {
    browser.goTo("/about-you/contact-details")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }

  def nationalityAndResidency(browser: TestBrowser) = {
    browser.goTo("/about-you/nationality-and-residency")
    browser.click("#nationality_British")
    browser.click("#resideInUK_answer_yes")
    browser.submit("button[type='submit']")
  }

  def nationalityAndResidencyNotBritishMarried(browser: TestBrowser) = {
    browser.goTo("/about-you/nationality-and-residency")
    browser.click("#nationality_Another_Nationality")
    browser.fill("#actualnationality") `with` "French"
    browser.click("#maritalStatus_Married_or_civil_partner")
    browser.click("#resideInUK_answer_yes")
    browser.submit("button[type='submit']")
  }

  def nationalityAndResidencyNotBritishSingle(browser: TestBrowser) = {
    browser.goTo("/about-you/nationality-and-residency")
    browser.click("#nationality_Another_Nationality")
    browser.fill("#actualnationality") `with` "French"
    browser.click("#maritalStatus_Single")
    browser.click("#resideInUK_answer_yes")
    browser.submit("button[type='submit']")
  }

  def nationalityAndResidencyNotBritishWithPartner(browser: TestBrowser) = {
    browser.goTo("/about-you/nationality-and-residency")
    browser.click("#nationality_Another_Nationality")
    browser.fill("#actualnationality") `with` "French"
    browser.click("#maritalStatus_Living_with_partner")
    browser.click("#resideInUK_answer_yes")
    browser.submit("button[type='submit']")
  }

  def otherEEAStateOrSwitzerland(browser: TestBrowser) = {
    browser.goTo("/about-you/other-eea-state-or-switzerland")
    browser.click("#eeaGuardQuestion_answer_no")
    browser.submit("button[type='submit']")
  }

  def claimDate(browser: TestBrowser) = {
    browser.goTo("/your-claim-date/claim-date")
    browser.fill("#dateOfClaim_day") `with` "1"
    browser.fill("#dateOfClaim_month") `with` "1"
    browser.fill("#dateOfClaim_year") `with` "2015"
    browser.click("#beforeClaimCaring_answer_no")
    browser.submit("button[type='submit']")
  }

  def employment(browser: TestBrowser) = {
    browser.goTo("/employment/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def justEmployment(browser: TestBrowser) = {
    browser.goTo("/employment/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_no")
    browser.submit("button[type='submit']")
  }

  def justSelfEmployment(browser: TestBrowser) = {
    browser.goTo("/employment/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def selfEmployment(browser: TestBrowser) = {
    browser.goTo("/employment/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def notInEmployment(browser: TestBrowser) = {
    browser.goTo("/employment/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_no")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_no")
    browser.submit("button[type='submit']")
  }

  // Your partner
  def yourPartnerPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/your-partner/personal-details")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Dave"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#otherNames") `with` "Roberts"
    browser.fill("#nationalInsuranceNumber_nino") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.fill("#dateOfBirth_day") `with` "3"
    browser.fill("#dateOfBirth_month") `with` "4"
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#separated_fromPartner_no")
    browser.click("#isPartnerPersonYouCareFor_yes")
    browser.submit("button[type='submit']")
  }

  // Your partner
  def yourPartnerPersonalDetailsPartnerPersonYouCareForNo(browser: TestBrowser) = {
    browser.goTo("/your-partner/personal-details")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Dave"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#otherNames") `with` "Roberts"
    browser.fill("#nationalInsuranceNumber_nino") `with` "AB123456C" // Pattern AB123456C
    browser.fill("#dateOfBirth_day") `with` "3"
    browser.fill("#dateOfBirth_month") `with` "4"
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#separated_fromPartner_no")
    browser.click("#isPartnerPersonYouCareFor_no")
    browser.submit("button[type='submit']")
  }

  def personYouCareForNotPartner(browser: TestBrowser) = {
    browser.goTo("/your-partner/person-you-care-for")
    browser.click("#isPartnerPersonYouCareFor_no")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.submit("button[type='submit']")
  }
  
  def yourPartnerCompleted(browser: TestBrowser) = {
    browser.goTo("/your-partner/completed")
    browser.submit("button[type='submit']")
  }
    
  // Care You Provide
  def theirPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-personal-details")
    browser.fill("#relationship") `with` "some other relationship"
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#dateOfBirth_day") `with` "3"
    browser.fill("#dateOfBirth_month") `with` "4"
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#theirAddress_answer_yes")
    browser.submit("button[type='submit']")
  }

  def theirPersonalDetailsNotLiveAtSameAddress(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-personal-details")
    browser.fill("#relationship") `with` "some other relationship"
    browser.fill("#title") `with` "Mr"
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#dateOfBirth_day") `with` "3"
    browser.fill("#dateOfBirth_month") `with` "4"
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#armedForcesPayment_yes")
    browser.click("#theirAddress_answer_no")
    browser.submit("button[type='submit']")
  }

  def theirContactDetails(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-contact-details")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.submit("button[type='submit']")
  }

  def moreAboutTheCare(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")

    browser.click("#beforeClaimCaring_answer_yes")
    browser.await().atMost(30, TimeUnit.SECONDS).until("#beforeClaimCaring_date_year").areDisplayed
    browser.fill("#beforeClaimCaring_date_day") `with` "3"
    browser.fill("#beforeClaimCaring_date_month") `with` "4"
    browser.fill("#beforeClaimCaring_date_year") `with` "1950"

//    browser.click("#hasSomeonePaidYou_yes")

    browser.submit("button[type='submit']")
  }

  def moreAboutTheCareWithNotPaying(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_yes")
    browser.click("#hasSomeonePaidYou_no")
    browser.fill("#careStartDate_day") `with` "3"
    browser.fill("#careStartDate_month") `with` "4"
    browser.fill("#careStartDate_year") `with` "1950"
    browser.submit("button[type='submit']")
  }
  
  def moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#beforeClaimCaring_answer_no")
    browser.submit("button[type='submit']")
  }


  def howWePayYou(browser: TestBrowser) = {
    browser.goTo("/pay-details/how-we-pay-you")
    browser.click("#likeToPay_yes")
    bankBuildingSocietyDetails(browser)
    browser.click("#paymentFrequency_"+PaymentFrequency.FourWeekly.replace(' ','_'))
    browser.submit("button[type='submit']")
  }

  def bankBuildingSocietyDetails(browser: TestBrowser) = {
    browser.fill("#bankDetails_accountHolderName") `with` "holder name"
    browser.fill("#bankDetails_bankFullName") `with` "bank name"
    browser.fill("#bankDetails_sortCode_sort1") `with` "10"
    browser.fill("#bankDetails_sortCode_sort2") `with` "11"
    browser.fill("#bankDetails_sortCode_sort3") `with` "12"
    browser.fill("#bankDetails_accountNumber") `with` "123456"
    browser.fill("#bankDetails_rollOrReferenceNumber") `with` "1234567"
  }

  def disclaimer(browser: TestBrowser) = {
    browser.goTo("/disclaimer/disclaimer")
    browser.submit("button[type='submit']")
  }

  def declaration(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/declaration")
    browser.click("#tellUsWhyFromAnyoneOnForm.informationFromPerson_no")
    browser.fill("#tellUsWhyFromAnyoneOnForm.whyPerson") `with` "Bar"
    browser.click("#confirm")
    browser.click("#someoneElse")
    browser.fill("#nameOrOrganisation") `with` "SomeOrg"
    browser.submit("button[type='submit']")
  }

  def declarationWithConsentYes(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/declaration")
    browser.click("#tellUsWhyFromAnyoneOnForm.informationFromPerson_yes")
    browser.click("#confirm")
    browser.click("#someoneElse")
    browser.fill("#nameOrOrganisation") `with` "SomeOrg"
    browser.submit("button[type='submit']")
  }

  def additionalInfo(browser: TestBrowser) = {
    browser.goTo("/information/additional-info")
    browser.click("#anythingElse_answer_no")
    browser.click("#welshCommunication_yes")
    browser.submit("button[type='submit']")
  }
  
  // Time Spent Abroad
  def normalResidenceAndCurrentLocation(browser: TestBrowser) = {
    browser.goTo("/time-spent-abroad/normal-residence-and-current-location")
    browser.click("#liveInUK_answer_yes")
    browser.click("#inGBNow_yes")
    browser.submit("button[value='next']")
  }
  
  def abroadForMoreThan4Weeks(browser: TestBrowser) = {
    browser.click("#anyTrips_no")
    browser.submit("button[value='next']")
  }
  
  def abroadForMoreThan52Weeks(browser: TestBrowser) = {
    browser.click("#anyTrips_no")
    browser.submit("button[value='next']")
  }

  // Education
  def yourCourseDetails(browser: TestBrowser) = {
    val courseTitle = "Law"
    val nameOfSchoolCollegeOrUniversity = "University"
    val nameOfMainTeacherOrTutor = "Mr Whiskers"
    val courseContactNumber = "12345678"

    browser.goTo("/education/your-course-details")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.fill("#courseTitle") `with` courseTitle
    browser.fill("#nameOfSchoolCollegeOrUniversity") `with` nameOfSchoolCollegeOrUniversity
    browser.fill("#nameOfMainTeacherOrTutor") `with` nameOfMainTeacherOrTutor
    browser.fill("#courseContactNumber") `with` courseContactNumber
    browser.fill("#startDate_day") `with` "16"
    browser.fill("#startDate_month") `with` "4"
    browser.fill("#startDate_year") `with` "1992"
    browser.fill("#expectedEndDate_day") `with` "30"
    browser.fill("#expectedEndDate_month") `with` "9"
    browser.fill("#expectedEndDate_year") `with` "1997"
    
    browser.submit("button[type='submit']")
  }
  
  def aboutOtherMoney(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#anyPaymentsSinceClaimDate_answer_yes")
    browser.fill("#whoPaysYou") `with` "The Man"
    browser.fill("#howMuch") `with` "12"
    browser.fill("#statutorySickPay_answer_no")
    browser.fill("#otherStatutoryPay_answer_no")
    browser.submit("button[type='submit']")
  }

  def aboutOtherMoneyInvalid(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#anyPaymentsSinceClaimDate_answer_no")
    browser.fill("#statutorySickPay_answer_yes")
    browser.fill("#otherStatutoryPay_answer_no")
    browser.submit("button[type='submit']")
  }

  def statutorySickPay(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#anyPaymentsSinceClaimDate_answer_no")
    browser.fill("#statutorySickPay_answer_yes")
    browser.fill("#otherStatutoryPay_answer_no")
    browser.submit("button[type='submit']")
  }

  def otherSickPay(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#anyPaymentsSinceClaimDate_answer_no")
    browser.fill("#statutorySickPay_answer_no")
    browser.fill("#otherStatutoryPay_answer_yes")
    browser.submit("button[type='submit']")
  }

  def aboutOtherMoneyWithAllStatutoryPay(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#anyPaymentsSinceClaimDate_answer_no")
    browser.fill("#statutorySickPay_answer_yes")
    browser.fill("#otherStatutoryPay_answer_yes")
    browser.submit("button[type='submit']")
  }

  def personContactDetails(browser:TestBrowser) = {
    browser.goTo("/other-money/person-contact-details")
    browser.submit("button[type='submit']")
  }

  def clickBackButton (browser:TestBrowser) = {
    browser.click("#backButton")
  }

}
