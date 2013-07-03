package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G5PreviousCarerContactDetailsIntegrationSpec extends Specification with Tags {

  "Previous Carer Contact Details" should {
    "be presented" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      browser.goTo("/careYouProvide/previousCarerContactDetails")
      browser.title mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      browser.goTo("/careYouProvide/previousCarerContactDetails")
      browser.title mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide"
      browser.fill("#postcode") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Previous Carer Person Details" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      browser.title mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // Landed on S4 G5
      browser.click("#backButton")
      browser.title mustEqual "Details Of The Person Who Claimed Before - Care You Provide" // Back to S4 G4
    }

    "navigate to next page on valid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/previousCarerContactDetails")
      browser.submit("button[type='submit']")
      browser.title mustEqual "Representatives For The Person - Care You Provide"
    }

    "contain the completed forms" in new WithBrowser {
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      FormHelper.fillPreviousCarerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section "integration"
}