package controllers.s7_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate

class G4AdditionalInformationIntegrationSpec extends Specification with Tags {
  "Additional Information" should {
    "be presented" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/additionalInfo")
      browser.title mustEqual "Additional Information - Consent And Declaration"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/consentAndDeclaration/additionalInfo")
      browser.title mustEqual "Additional Information - Consent And Declaration"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.additionalInfo(browser)
      browser.title mustEqual "Submit - Consent And Declaration"
    }

    "navigate back to Declaration" in new WithBrowser {
      Formulate.declaration(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Declaration - Consent And Declaration"
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.additionalInfo(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}