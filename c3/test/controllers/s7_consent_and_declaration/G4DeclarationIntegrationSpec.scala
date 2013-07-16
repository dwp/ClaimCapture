package controllers.s7_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate

class G4DeclarationIntegrationSpec extends Specification with Tags {
  "Declaration" should {
    "be presented" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/declaration")
      browser.title mustEqual "Declaration - Consent And Declaration"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/declaration")
      browser.title mustEqual "Declaration - Consent And Declaration"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }



    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.declaration(browser)
      browser.title mustEqual "Submit - Consent And Declaration"
    }

    "navigate back to Disclaimer" in new WithBrowser {
      Formulate.disclaimer(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Disclaimer - Consent And Declaration"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.declaration(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}