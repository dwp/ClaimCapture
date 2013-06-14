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
    browser.fill("#address") `with` ("My Address")
    browser.fill("#postcode") `with` ("SE1 6EH")
    browser.submit("button[type='submit']")
  }

}
