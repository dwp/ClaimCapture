package controllers

import play.api.test.TestBrowser
import java.util.concurrent.TimeUnit

object FormHelper {

  def fillYourDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
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

  def fillYourDetailsEnablingTimeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
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

  def fillYourContactDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/contactDetails")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }

  def fillTimeOutsideUK(browser: TestBrowser) = {
    browser.goTo("/aboutyou/timeOutsideUK")
    browser.click("#currentlyLivingInUK_no")
    browser.submit("button[value='next']")
  }

  def fillClaimDate(browser: TestBrowser) = {
    browser.goTo("/aboutyou/claimDate")
    browser.click("#dateOfClaim_day option[value='3']")
    browser.click("#dateOfClaim_month option[value='4']")
    browser.fill("#dateOfClaim_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def fillMoreAboutYou(browser: TestBrowser) = {
    browser.goTo("/aboutyou/moreAboutYou")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def fillEmployment(browser: TestBrowser) = {
    browser.goTo("/aboutyou/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def fillPropertyAndRent(browser: TestBrowser) = {
    browser.goTo("/aboutyou/propertyAndRent")
    browser.click("#ownProperty_yes")
    browser.click("#hasSublet_yes")
    browser.submit("button[type='submit']")
  }
  
  // Your partner
  def fillYourPartnerPersonalDetails(browser: TestBrowser) = {
    browser.waitUntil[Unit](30, TimeUnit.SECONDS){
      browser.goTo("/yourPartner/personalDetails")
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
      browser.click("#liveAtSameAddress_yes]")
      browser.submit("button[type='submit']")
    }
  }
    
  // Care You Provide
  def fillTheirPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirPersonalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_yes]")
    browser.submit("button[type='submit']")
  }

  def fillTheirPersonalDetailsNotLiveAtSameAddress(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirPersonalDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.click("#liveAtSameAddress_no]")
    browser.submit("button[type='submit']")
  }

  def fillTheirContactDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirContactDetails")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.submit("button[type='submit']")
  }

  def fillMoreAboutTheCare(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutTheCare")
    browser.click("#spent35HoursCaring_yes")

    browser.click("#spent35HoursCaringBeforeClaim_yes")
    browser.await().atMost(30, TimeUnit.SECONDS).until("#careStartDate_year").areDisplayed
    browser.click("#careStartDate_day option[value='3']")
    browser.click("#careStartDate_month option[value='4']")
    browser.fill("#careStartDate_year") `with` "1950"

    browser.click("#hasSomeonePaidYou_yes")

    browser.submit("button[type='submit']")
  }

  def fillMoreAboutTheCareWithNotPaying(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutTheCare")
    browser.click("#spent35HoursCaring_yes")
    browser.click("#spent35HoursCaringBeforeClaim_yes")
    browser.click("#hasSomeonePaidYou_no")
    browser.click("#careStartDate_day option[value='3']")
    browser.click("#careStartDate_month option[value='4']")
    browser.fill("#careStartDate_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def fillMoreAboutThePersonWithClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutThePerson")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_yes")
    browser.click("#claimedAllowanceBefore_yes")
    browser.submit("button[type='submit']")
  }

  def fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/moreAboutThePerson")
    browser.click("#relationship option[value='father']")
    browser.click("#armedForcesPayment_no")
    browser.click("#claimedAllowanceBefore_no")
    browser.submit("button[type='submit']")
  }

  def fillPreviousCarerPersonalDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/previousCarerPersonalDetails")
    browser.fill("#firstName") `with` "John"
    browser.fill("#middleName") `with` "Joe"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#nationalInsuranceNumber_ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#nationalInsuranceNumber_ni2") `with` "12"
    browser.fill("#nationalInsuranceNumber_ni3") `with` "34"
    browser.fill("#nationalInsuranceNumber_ni4") `with` "56"
    browser.fill("#nationalInsuranceNumber_ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def fillPreviousCarerContactDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/previousCarerContactDetails")
    browser.fill("#address_lineOne") `with` "My Address"
    browser.fill("#postcode") `with` "SE1 6EH"
    browser.submit("button[type='submit']")
  }
  
  def fillRepresentativesForThePerson(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/representativesForPerson")
    browser.click("#actForPerson_yes")
    browser.click("#someoneElseActForPerson_yes")
    browser.click("#actAs option[value='guardian']")
    browser.click("#someoneElseActAs option[value='judicial']")
    browser.fill("#someoneElseFullName") `with` "John"
    browser.submit("button[type='submit']")
  }
  
  def fillRepresentativesForThePersonNegativeAnswers(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/representativesForPerson")
    browser.click("#actForPerson_no")
    browser.click("#someoneElseActForPerson_no")
    browser.submit("button[type='submit']")
  }
}