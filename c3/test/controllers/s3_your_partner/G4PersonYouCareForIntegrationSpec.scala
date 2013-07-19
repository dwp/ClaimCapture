package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import org.specs2.execute.PendingUntilFixed

class G4PersonYouCareForIntegrationSpec extends Specification with Tags with PendingUntilFixed {

  "Person You Care For" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/yourPartner/personYouCareFor")
      titleMustEqual("Person You Care For - Your Partner")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/yourPartner/personYouCareFor")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      browser.goTo("/yourPartner/personYouCareFor")
      browser.click("#backButton")
      titleMustNotEqual("Person You Care For - Your Partner")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.personYouCareFor(browser)
      titleMustEqual("Completion - Your Partner")
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerSeparated(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 3
    }
  } section "integration"
}