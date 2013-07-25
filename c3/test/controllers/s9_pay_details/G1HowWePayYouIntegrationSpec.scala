package controllers.s9_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G1HowWePayYouIntegrationSpec extends Specification with Tags {
  "How we pay you" should {
    "be presented" in new WithBrowser {
      browser.goTo("/payDetails/howWePayYou")
      browser.title mustEqual "How We Pay You - Pay Details"
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")
      browser.goTo("/payDetails/howWePayYou")
      titleMustEqual("Additional Information - Consent And Declaration")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/payDetails/howWePayYou")
      browser.title mustEqual "How We Pay You - Pay Details"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.howWePayYou(browser)
      browser.title mustEqual "Bank Building Society Details - Pay Details"
    }

    "navigate back to Other Money - Completed" in new WithBrowser {
      browser.goTo("/payDetails/howWePayYou")
      browser.click(".form-steps a")
      //Other Income completed page does a redirect to first page
      browser.title mustEqual "About Other Money - Other Money"

    }

    "contain the completed forms" in new WithBrowser {
      Formulate.howWePayYou(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}