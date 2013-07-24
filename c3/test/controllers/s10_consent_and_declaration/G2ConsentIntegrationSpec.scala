package controllers.s10_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2ConsentIntegrationSpec extends Specification with Tags {
  "Consent" should {
    "be presented" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/consent")
      browser.title mustEqual "Consent - Consent And Declaration"
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consentAndDeclaration/consent")
      titleMustEqual("Consent - Consent And Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Consent - Consent And Declaration")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.consent(browser)
      browser.title mustEqual "Disclaimer - Consent And Declaration"
    }

    "navigate back to AdditionalInformation" in new WithBrowser {
      Formulate.additionalInfo(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Additional Information - Consent And Declaration"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.consent(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}