package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G4DeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consentAndDeclaration/declaration")
      titleMustEqual("Declaration - Consent And Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consentAndDeclaration/declaration")
      titleMustEqual("Declaration - Consent And Declaration")

      browser.submit("button[type='submit']")
      titleMustEqual("Declaration - Consent And Declaration")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.declaration(browser)
      titleMustEqual("Submit - Consent And Declaration")
    }

    "navigate back to Disclaimer" in new WithBrowser with BrowserMatchers {
      Formulate.disclaimer(browser)
      titleMustEqual("Declaration - Consent And Declaration")

      browser.click(".form-steps a")
      titleMustEqual("Disclaimer - Consent And Declaration")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.declaration(browser)
      titleMustEqual("Submit - Consent And Declaration")

      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section("integration",models.domain.ConsentAndDeclaration.id)
}