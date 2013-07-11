package controllers.s7_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate

class G1ConsentIntegrationSpec extends Specification with Tags {
  "Consent" should {
    "be presented" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/consent")
      browser.title mustEqual "Consent - Consent And Declaration"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/consent")
      browser.title mustEqual "Consent - Consent And Declaration"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 3
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.consent(browser)
      browser.title mustEqual "Disclaimer - Consent And Declaration"
    }

    "navigate back to Pay Details - Completed" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/consent")
      browser.click(".form-steps a")
      browser.title mustEqual "Completion - Pay Details"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.consent(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}