package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import play.api.i18n.Messages

class G1HowWePayYouIntegrationSpec extends Specification with Tags {
  "How we pay you" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("How We Pay You - Pay Details")
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual(Messages("s2.g5") + " - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("Additional Information - Consent And Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      titleMustEqual("How We Pay You - Pay Details")

      browser.submit("button[type='submit']")
      titleMustEqual("How We Pay You - Pay Details")

      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      titleMustEqual("Bank Building Society Details - Pay Details")
    }

    "navigate back to Other Money - Completed" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/how-we-pay-you")
      browser.click(".form-steps a")
      //Other Income completed page does a redirect to first page
      titleMustEqual("Details about other money - About Other Money")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      findMustEqualSize("div[class=completed] ul li", 1)
    }
  } section("integration", models.domain.PayDetails.id)
}