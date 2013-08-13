package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import controllers.BrowserMatchers

class G1AdditionalInformationIntegrationSpec extends Specification with Tags {
  "Additional Information" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/additional-info")
      titleMustEqual("Additional Information - Consent And Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/additional-info")
      titleMustEqual("Additional Information - Consent And Declaration")
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      titleMustEqual("Consent - Consent And Declaration")
    }

    "navigate back to Pay Details - Completed" in new WithBrowser with BrowserMatchers {
      browser.goTo("/consent-and-declaration/additional-info")
      browser.click(".form-steps a")
      titleMustEqual("Completion - How we pay you")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.additionalInfo(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
  } section("integration", models.domain.ConsentAndDeclaration.id)
}