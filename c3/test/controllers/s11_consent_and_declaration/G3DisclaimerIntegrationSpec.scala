package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate

class G3DisclaimerIntegrationSpec extends Specification with Tags {
  "Disclaimer" should {
    "be presented" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/disclaimer")
      browser.title mustEqual "Disclaimer - Consent And Declaration"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/disclaimer")
      browser.title mustEqual "Disclaimer - Consent And Declaration"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.disclaimer(browser)
      browser.title mustEqual "Declaration - Consent And Declaration"
    }

    "navigate back to Consent" in new WithBrowser {
      Formulate.consent(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Consent - Consent And Declaration"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.disclaimer(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section("integration",models.domain.ConsentAndDeclaration.id)
}