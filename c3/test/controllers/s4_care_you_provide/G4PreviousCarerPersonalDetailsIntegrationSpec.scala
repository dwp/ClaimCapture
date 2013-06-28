package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import integration.Helper
import java.util.concurrent.TimeUnit

class G4PreviousCarerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Previous Carer Personal Details" should {
    "navigate to Previous Carer Details, if anyone else claimed allowance for this person before" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.title() mustEqual "Details Of The Person Who Claimed Before - Care You Provide"
    }

    "navigate to Representatives For The Person, if nobody claimed allowance for this person before" in new WithBrowser {
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      browser.title() mustEqual "Representatives For The Person - Care You Provide"
    }

    "navigate to Previous Carer Contact Details on submission of empty form" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.submit("button[type='submit']")
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide"
    }

    "navigate to Previous Carer Contact Details on submission of completed form" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      Helper.fillPreviousCarerPersonalDetails(browser)
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.fill("#nationalInsuranceNumber_ni1") `with` "12345"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to More About The Person You Care For" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.click("#backButton")
      browser.title() mustEqual "More About The Person You Care For - Care You Provide"
    }

    "contain the completed forms" in new WithBrowser {
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigating forward and back presents the same completed question list" in new WithBrowser {
      Helper.fillTheirPersonalDetails(browser)
      Helper.fillTheirContactDetails(browser)
      Helper.fillMoreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size mustEqual 3
      Helper.fillPreviousCarerPersonalDetails(browser)
      browser.title() mustEqual "Contact Details Of The Person Who Claimed Before - Care You Provide" // DELETE
      browser.find("div[class=completed] ul li").size mustEqual 4
      browser.click("#backButton")

      TimeUnit.SECONDS.sleep(2)
      browser.find("div[class=completed] ul li").size mustEqual 3
    }
  } section "integration"
}