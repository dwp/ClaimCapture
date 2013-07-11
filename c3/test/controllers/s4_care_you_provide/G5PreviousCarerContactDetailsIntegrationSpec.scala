package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, Formulate}
import play.api.test.WithBrowser

class G5PreviousCarerContactDetailsIntegrationSpec extends Specification with Tags {

  "Previous Carer Contact Details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      browser.goTo("/careYouProvide/previousCarerContactDetails")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      browser.goTo("/careYouProvide/previousCarerContactDetails")
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      browser.fill("#postcode") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Previous Carer Person Details" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      browser.click("#backButton")
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/previousCarerContactDetails")
      browser.submit("button[type='submit']")
      titleMustEqual("Representatives For The Person - Care You Provide")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section "integration"
}