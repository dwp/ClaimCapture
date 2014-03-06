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
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "contain errors on invalid submission with employment" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      browser.goTo("/consent-and-declaration/consent")
      titleMustEqual("Consent - Consent and Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Consent - Consent and Declaration")
      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "contain errors on invalid submission with aboutOtherMoney" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.aboutOtherMoneyInvalid(browser)
      browser.goTo("/consent-and-declaration/consent")
      titleMustEqual("Consent - Consent and Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Consent - Consent and Declaration")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "navigate to next page on valid submission (both no)" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.consent(browser)
      titleMustEqual("Disclaimer - Consent and Declaration")
    }
    
    "navigate to next page on valid submission (both yes)" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.consentBothYes(browser)
      titleMustEqual("Disclaimer - Consent and Declaration")
    }

    "navigate back to AdditionalInformation" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      browser.click(".form-steps a")
      titleMustEqual("Additional information - Consent and Declaration")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.consent(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}