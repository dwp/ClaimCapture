package integration

import play.api.test.TestBrowser

object Helper {

  def fillYourDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#ni2") `with` "12"
    browser.fill("#ni3") `with` "34"
    browser.fill("#ni4") `with` "56"
    browser.fill("#ni5") `with` "C"
    browser.click("#dateOfBirth_day option[value='3']")
    browser.click("#dateOfBirth_month option[value='4']")
    browser.fill("#dateOfBirth_year") `with` "1950"
    browser.fill("#nationality") `with` "English"
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_yes")
    browser.submit("button[type='submit']")
  }

  def fillYourDetailsEnablingTimeOutsideUK(browser:TestBrowser) ={
    browser.goTo("/aboutyou/yourDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` "John"
    browser.fill("#surname") `with` "Appleseed"
    browser.fill("#ni1") `with` "AB" // Pattern AB123456C
    browser.fill("#ni2") `with` "12"
    browser.fill("#ni3") `with` "34"
    browser.fill("#ni1") `with` "56"
    browser.fill("#ni1") `with` "C"
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

  def fillTimeOutsideUK(browser:TestBrowser) = {
    browser.goTo("/aboutyou/timeOutsideUK")
    browser.click("#currentlyLivingInUK_yes")
    browser.submit("button[value='next']")
  }

  def fillClaimDate(browser:TestBrowser) = {
    browser.goTo("/aboutyou/claimDate")
    browser.click("#dateOfClaim_day option[value='3']")
    browser.click("#dateOfClaim_month option[value='4']")
    browser.fill("#dateOfClaim_year") `with` "1950"
    browser.submit("button[type='submit']")
  }

  def fillMoreAboutYou(browser:TestBrowser) = {
    browser.goTo("/aboutyou/moreAboutYou")
    browser.click("#hadPartnerSinceClaimDate_yes")
    browser.click("#eitherClaimedBenefitSinceClaimDate_yes")
    browser.click("#beenInEducationSinceClaimDate_yes")
    browser.click("#receiveStatePension_yes")
    browser.submit("button[type='submit']")
  }

  def fillEmployment(browser:TestBrowser) = {
    browser.goTo("/aboutyou/employment")
    browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
    browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
    browser.submit("button[type='submit']")
  }

  def fillPropertyAndRent(browser:TestBrowser) = {
    browser.goTo("/aboutyou/propertyAndRent")
    browser.click("#ownProperty_yes")
    browser.click("#hasSublet_yes")
    browser.submit("button[type='submit']")
  }

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

  def fillTheirContactDetails(browser: TestBrowser) = {
    browser.goTo("/careYouProvide/theirContactDetails")
    browser.fill("#address_lineOne") `with` "Their Address"
    browser.fill("#postcode") `with` "RM11 1DA"
    browser.submit("button[type='submit']")
  }

}