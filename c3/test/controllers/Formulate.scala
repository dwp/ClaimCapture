package controllers

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit
import app.AccountStatus

object Formulate {
  val partnerAddress = "Partner Address"

  val partnerPostcode = "RM11 1AA"
    
  def yourDetails(browser: TestBrowser) = {
    browser.goTo("/about-you/your-details")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.fill("#nationality") `with` "English"
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_yes")
    browser.submit("button[type='submit']")
  }

  def yourDetailsEnablingTimeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/about-you/your-details")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.fill("#nationality") `with` "English"
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_no")
    browser.submit("button[type='submit']")
  }

  def yourContactDetails(browser: TestBrowser) = {
    browser.goTo("/about-you/contact-details")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }

  def timeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/about-you/time-outside-uk")
    browser.click("#livingInUK_answer_yes")

    browser.click("#livingInUK_arrivalDate_day option[value='1']")
    browser.click("#livingInUK_arrivalDate_month option[value='1']")
    browser.fill("#livingInUK_arrivalDate_year") `with` "2001"

    browser.click("#livingInUK_goBack_answer_no")

    browser.submit("button[value='next']")
  }

  def timeOutsideUKNotLivingInUK(browser: TestBrowser) = {
    browser.goTo("/about-you/time-outside-uk")
    browser.click("#livingInUK_answer_no")
    browser.submit("button[value='next']")
  }

  def claimDate(browser: TestBrowser) = {
    browser.goTo("/about-you/claim-date")
    browser.click("#dateOfClaim_day option[value='3']")
    browser.click("#dateOfClaim_month option[value='4']")
    browser.fill("#dateOfClaim_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def moreAboutYou(browser: TestBrowser) = {
    browser.goTo("/about-you/more-about-you")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def moreAboutYouNotHadPartnerSinceClaimDate(browser: TestBrowser) = {
    browser.goTo("/about-you/more-about-you")
    browser.click("#hadPartnerSinceClaimDate_no")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }
  
  def moreAboutYouNotBeenInEducationSinceClaimDate(browser: TestBrowser) = {
    browser.goTo("/about-you/more-about-you")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_no")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def employment(browser: TestBrowser) = {
    browser.goTo("/about-you/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  // Your partner
  def yourPartnerPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/your-partner/personal-details")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Dave"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#otherNames") `with` "Roberts"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#separated_fromPartner_no]")
    browser.submit("button[type='submit']")
  }

  def personYouCareFor(browser: TestBrowser) = {
    browser.goTo("/your-partner/person-you-care-for")
    browser.click("#isPartnerPersonYouCareFor_yes]")
    browser.submit("button[type='submit']")
  }
  
  def personYouCareForNotPartner(browser: TestBrowser) = {
    browser.goTo("/your-partner/person-you-care-for")
    browser.click("#isPartnerPersonYouCareFor_no]")
    browser.submit("button[type='submit']")
  }
  
  def yourPartnerCompleted(browser: TestBrowser) = {
    browser.goTo("/your-partner/completed")
    browser.submit("button[type='submit']")
  }
    
  // Care You Provide
  def theirPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-personal-details")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddressCareYouProvide_yes]")
    browser.submit("button[type='submit']")
  }

  def theirPersonalDetailsNotLiveAtSameAddress(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-personal-details")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddressCareYouProvide_no]")
    browser.submit("button[type='submit']")
  }

  def theirContactDetails(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-contact-details")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.submit("button[type='submit']")
  }
  
  def theirContactDetailsInvalidPhoneNumber(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/their-contact-details")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.fill("#phoneNumber") `with` "INVALID"
    browser.submit("button[type='submit']")
  }

  def moreAboutThePersonWithClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/relationship-and-other-claims")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_yes")
    browser.click("#claimedAllowanceBefore_yes")
    browser.submit("button[type='submit']")
  }

  def moreAboutThePersonWithNotClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/relationship-and-other-claims")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_no")
    browser.click("#claimedAllowanceBefore_no")
    browser.submit("button[type='submit']")
  }

  def moreAboutTheCare(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")

    browser.click("#beforeClaimCaring_answer_yes")
    browser.await().atMost(30, TimeUnit.SECONDS).until("#beforeClaimCaring_date_year").areDisplayed
    browser.click("#beforeClaimCaring_date_day option[value='3']")
    browser.click("#beforeClaimCaring_date_month option[value='4']")
    browser.fill("#beforeClaimCaring_date_year") `with` "1950"

    browser.click("#hasSomeonePaidYou_yes")

    browser.submit("button[type='submit']")
  }

  def moreAboutTheCareWithNotPaying(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_yes")
    browser.click("#hasSomeonePaidYou_no")
    browser.click("#careStartDate_day option[value='3']")
    browser.click("#careStartDate_month option[value='4']")
    browser.fill("#careStartDate_year") `with` "1950"
    browser.submit("button[type='submit']")
  }
  
  def moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser: TestBrowser) = {
    browser.goTo("/care-you-provide/more-about-the-care")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_no")
    browser.click("#hasSomeonePaidYou_yes")
    browser.submit("button[type='submit']")
  }



  def howWePayYou(browser: TestBrowser) = {
    browser.goTo("/pay-details/how-we-pay-you")
    browser.click("#likeToPay_" + AccountStatus.BankBuildingAccount.name)
    browser.click("#paymentFrequency option[value='fourWeekly']")
    browser.submit("button[type='submit']")
  }

  def bankBuildingSocietyDetails(browser: TestBrowser) = {
    browser.goTo("/pay-details/bank-building-society-details")
    browser.fill("#accountHolderName") `with` "holder name"
    browser.fill("#bankFullName") `with` "bank name"
    browser.fill("#sortCode_sort1") `with` "10"
    browser.fill("#sortCode_sort2") `with` "11"
    browser.fill("#sortCode_sort3") `with` "12"
    browser.fill("#accountNumber") `with` "account"
    browser.fill("#rollOrReferenceNumber") `with` "1234567"
    browser.submit("button[type='submit']")
  }

  def consent(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/consent")
    browser.click("#gettingInformationFromAnyEmployer_informationFromEmployer_no")
    browser.fill("#gettingInformationFromAnyEmployer_why") `with` "Foo"
    browser.click("#tellUsWhyEmployer_informationFromPerson_no")
    browser.fill("#tellUsWhyEmployer_whyPerson") `with` "Bar"
    browser.submit("button[type='submit']")
  }
  
  def consentBothYes(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/consent")
    browser.click("#gettingInformationFromAnyEmployer_informationFromEmployer_yes")
    browser.click("#tellUsWhyEmployer_informationFromPerson_yes")
    browser.submit("button[type='submit']")
  }

  def disclaimer(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/disclaimer")
    browser.click("#read")
    browser.submit("button[type='submit']")
  }

  def declaration(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/declaration")
    browser.click("#confirm")
    browser.click("#someoneElse")
    browser.submit("button[type='submit']")
  }

  def additionalInfo(browser: TestBrowser) = {
    browser.goTo("/consent-and-declaration/additional-info")
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
  
  def otherEEAStateOrSwitzerland(browser: TestBrowser) = {
    browser.click("#benefitsFromOtherEEAStateOrSwitzerland_answer_no")
    browser.click("#workingForOtherEEAStateOrSwitzerland_no")
    browser.submit("button[value='next']")
  }
  
  // Education
  def yourCourseDetails(browser: TestBrowser) = {
    val courseType = "University"
    val courseTitle = "Law"
    val startDateDay = "16"
    val studentReferenceNumber = "ST-2828281"

    browser.goTo("/education/your-course-details")
    browser.fill("#courseType") `with` courseType
    browser.fill("#courseTitle") `with` courseTitle
    browser.click("#startDate_day option[value='16']")
    browser.click("#startDate_month option[value='4']")
    browser.fill("#startDate_year") `with` "1992"
    browser.click("#expectedEndDate_day option[value='30']")
    browser.click("#expectedEndDate_month option[value='9']")
    browser.fill("#expectedEndDate_year") `with` "1997"
    browser.click("#finishedDate_day option[value='1']")
    browser.click("#finishedDate_month option[value='1']")
    browser.fill("#finishedDate_year") `with` "2000"
    browser.fill("#courseTitle") `with` courseTitle
    
    browser.submit("button[type='submit']")
  }
  
  def addressOfSchoolCollegeOrUniversity(browser: TestBrowser) = {
    val nameOfSchoolCollegeOrUniversity = "MIT"
    val nameOfMainTeacherOrTutor = "Albert Einstein"
    val addressLineOne = "123 Street"
    val postcode: String = "SE1 6EH"
    val phoneNumber = "02076541058"
    val faxNumber = "07076541058"
    
    browser.goTo("/education/address-of-school-college-or-university")
    browser.fill("#nameOfSchoolCollegeOrUniversity") `with` nameOfSchoolCollegeOrUniversity
    browser.fill("#nameOfMainTeacherOrTutor") `with` nameOfMainTeacherOrTutor
    browser.fill("#address_lineOne") `with` addressLineOne
    browser.fill("#postcode") `with` postcode
    browser.fill("#phoneNumber") `with` phoneNumber
    browser.fill("#faxNumber") `with` faxNumber
    browser.submit("button[type='submit']")
  }
  
  def aboutOtherMoney(browser: TestBrowser) = {
    browser.goTo("/other-money/about-other-money")
    browser.click("#yourBenefits_answer_yes")
    browser.fill("#yourBenefits_text1") `with` "Bar"
    browser.fill("#yourBenefits_text2") `with` "Claimed"
    browser.submit("button[type='submit']")
  }

  def personContactDetails(browser:TestBrowser) = {
    browser.goTo("/other-money/person-contact-details")
    browser.submit("button[type='submit']")
  }
}