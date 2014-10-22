package controllers

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags
import play.api.test.WithBrowser

class CompletedQuestionGroupListSpec extends Specification with Tags {
  "Completed Question Group List" should {
    "increase when navigating forwards" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More about the care you provide - About the care you provide")
      browser.find("div[class=completed] ul li").size mustEqual 2
    }

    "decrease when navigating backwards" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More about the care you provide - About the care you provide")
      browser.find("div[class=completed] ul li").size mustEqual 2

      browser.click("#backButton")
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      browser.find("div[class=completed] ul li").size mustEqual 1
    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore positive answer path" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.find("div[class=completed] ul li").size mustEqual 3
      browser.find("div[class=completed] ul li").get(0).getText must contain("Details of the person you care for")
      browser.find("div[class=completed] ul li").get(1).getText must contain("Their contact details")

    }

    "contain the correct items when navigating S4G3 ClaimedAllowanceBefore negative answer path" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.find("div[class=completed] ul li").size mustEqual 3
      browser.find("div[class=completed] ul li").get(1).getText must contain("More about the Person you care for")
    }
  } section "integration"
}