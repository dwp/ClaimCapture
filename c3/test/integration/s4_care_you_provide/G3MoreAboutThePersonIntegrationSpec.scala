package integration.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import integration.Helper
import play.api.test.WithBrowser

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
      Helper.fillTheirContactDetails(browser)
      browser.click("#backButton")
      browser.title() mustEqual "Their Contact Details - Care You Provide"
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "navigate to Previous Carer Details, if anyone else claimed allowance for this person before" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.title() mustEqual "Details Of The Person Who Claimed Before - Care You Provide"
    }

    "navigate to Representatives For The Person, if nobody claimed allowance for this person before" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }
  } section "integration"
}