package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G4PreviousCarerPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Previous Carer Personal Details" should {
    "navigate to Previous Carer Details, if anyone else claimed allowance for this person before" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
    }

    "navigate to Representatives For The Person, if nobody claimed allowance for this person before" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/previousCarerPersonalDetails")
      titleMustEqual("Representatives For The Person - Care You Provide")
    }

    "navigate to Previous Carer Contact Details on submission of empty form" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.submit("button[type='submit']")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
    }

    "navigate to Previous Carer Contact Details on submission of completed form" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.fill("#nationalInsuranceNumber_ni1") `with` "12345"
      browser.submit("button[type='submit']")
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to More About The Person You Care For" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.click("#backButton")
      titleMustEqual("More About The Person You Care For - Care You Provide")
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigating forward and back presents the same completed question list" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 3

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 4

      browser.click("#backButton")
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
      browser.find("div[class=completed] ul li").size mustEqual 3
    }
  } section "integration"
}