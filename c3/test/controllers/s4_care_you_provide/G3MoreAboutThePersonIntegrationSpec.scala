package controllers.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G3MoreAboutThePersonIntegrationSpec extends Specification with Tags {

  "More About The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/relationshipAndOtherClaims")
      titleMustEqual("Relationship and other claims - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/relationshipAndOtherClaims")
      browser.submit("button[type='submit']")
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to Their Contact Details" in new WithBrowser with BrowserMatchers {
      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.click("#backButton")
      titleMustEqual("Contact details of the person you care for - About the care you provide")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")
      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "navigate to Previous Carer Details when submitting with claimedAllowanceBefore positive" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")
    }

    "navigate to Representatives For The Person when submitting with claimedAllowanceBefore negative" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithNotClaimedAllowanceBefore(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")
    }
  } section("integration",models.domain.CareYouProvide.id)
}