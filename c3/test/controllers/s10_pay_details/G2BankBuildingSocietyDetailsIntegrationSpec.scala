package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2BankBuildingSocietyDetailsIntegrationSpec extends Specification with Tags {
  "Bank building society details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/bank-building-society-details")
      titleMustEqual("Bank Building Society Details - Pay Details")
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      browser.goTo("/pay-details/bank-building-society-details")
      titleMustEqual("Additional Information - Consent And Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/bank-building-society-details")
      titleMustEqual("Bank Building Society Details - Pay Details")
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 5)
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.bankBuildingSocietyDetails(browser)
      titleMustEqual("Completion - Pay Details")
    }

    "navigate back to How We Pay You - Pay Details" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      titleMustEqual("Bank Building Society Details - Pay Details")

      browser.goTo("/pay-details/bank-building-society-details")
      browser.click("#backButton")
      titleMustEqual("How would you like to get paid? - How we pay you")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      Formulate.bankBuildingSocietyDetails(browser)
      findMustEqualSize("div[class=completed] ul li", 2)
    }
  } section("integration", models.domain.PayDetails.id)
}