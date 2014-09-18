package controllers.s12_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import controllers.BrowserMatchers

class G2DisclaimerIntegrationSpec extends Specification with Tags {
  "Disclaimer" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/disclaimer")
      titleMustEqual("Disclaimer - Consent and Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/disclaimer")
      titleMustEqual("Disclaimer - Consent and Declaration")
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.disclaimer(browser)
      titleMustEqual("Declaration - Consent and Declaration")
    }

    "navigate back to additional information" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.additionalInfo(browser)
      browser.click(".form-steps a")
      titleMustEqual("Additional information - Information")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.disclaimer(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}