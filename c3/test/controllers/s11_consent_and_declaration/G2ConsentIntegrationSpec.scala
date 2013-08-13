package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2ConsentIntegrationSpec extends Specification with Tags {
  "Consent" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/consent")
      titleMustEqual("Consent - Consent and Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/consent")
      titleMustEqual("Consent - Consent and Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Consent - Consent and Declaration")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.consent(browser)
      titleMustEqual("Disclaimer - Consent and Declaration")
    }

    "navigate back to AdditionalInformation" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      browser.click(".form-steps a")
      titleMustEqual("Additional Information - Consent and Declaration")
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.consent(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}