package integration

import play.api.test.TestBrowser

object Helper {

  def fillYourDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/yourDetails")
    browser.click("#title option[value='mr']")
    browser.fill("#firstName") `with` ("John")
    browser.fill("#surname") `with` ("Appleseed")
    browser.click("#dateOfBirth-day option[value='3']")
    browser.click("#dateOfBirth-month option[value='4']")
    browser.fill("#dateOfBirth-year") `with` ("1950")
    browser.fill("#nationality") `with` ("English")
    browser.click("#maritalStatus option[value='s']")
    browser.click("#alwaysLivedUK_yes")
    browser.submit("button[type='submit']")
  }

  def fillContactDetails(browser: TestBrowser) = {
    browser.goTo("/aboutyou/contactDetails")
    browser.fill("#address-lineOne") `with` ("My Address")
    browser.fill("#postcode") `with` ("SE1 6EH")
    browser.submit("button[type='submit']")
  }

  def fillClaimDate(browser:TestBrowser) = {
    browser.goTo("/aboutyou/claimDate")
    browser.click("#dateOfClaim-day option[value='3']")
    browser.click("#dateOfClaim-month option[value='4']")
    browser.fill("#dateOfClaim-year") `with` ("1950")
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

}
