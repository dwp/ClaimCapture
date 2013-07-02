package controllers.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import controllers.FormHelper

class G3MoreAboutThePersonIntegrationSpec extends Specification with Tags {

  "More About The Person" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutThePerson")
      browser.title() mustEqual "More About The Person You Care For - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/moreAboutThePerson")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to Their Contact Details" in new WithBrowser {
      FormHelper.fillTheirContactDetails(browser)
      browser.click("#backButton")
      browser.title() mustEqual "Their Contact Details - Care You Provide"
    }

    "contain the completed forms" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      FormHelper.fillTheirContactDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "navigate to Previous Carer Details when submitting with claimedAllowanceBefore positive" in new WithBrowser {
      FormHelper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.title() mustEqual "Details Of The Person Who Claimed Before - Care You Provide"
    }

    "navigate to Representatives For The Person when submitting with claimedAllowanceBefore negative" in new WithBrowser {
      FormHelper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }
  } section "integration"
}