package controllers.s4_care_you_provide

import org.specs2.mutable.{ Tags, Specification }
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G3RelationshipAndOtherClaimsIntegrationSpec extends Specification with Tags {

  "More About The Person" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/relationship-and-other-claims")
      titleMustEqual("Relationship and other claims - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/relationship-and-other-claims")
      browser.submit("button[type='submit']")
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to Their Contact Details" in new WithBrowser with BrowserMatchers {
      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.click("#backButton")
      titleMustEqual("Contact details of the Person you care for - About the care you provide")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the Person you care for - About the care you provide")
      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "navigate to More about the care when submitting with claimedAllowanceBefore positive" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("More about the care you provide - About the care you provide")
    }

  } section("integration", models.domain.CareYouProvide.id)
}