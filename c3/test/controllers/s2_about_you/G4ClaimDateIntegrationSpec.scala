package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, Formulate}
import play.api.test.WithBrowser
import play.api.i18n.Messages

class G4ClaimDateIntegrationSpec extends Specification with Tags {
  "Claim Date" should {
    sequential

    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/about-you/claim-date")
      titleMustEqual("Your Claim Date - About you - the carer")
    }

    "contain 2 completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)

      titleMustEqual("Your Claim Date - About you - the carer")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "fill date" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)

      titleMustEqual(Messages("s2.g5") + " - About you - the carer")
      browser.find("div[class=completed] ul li h3").get(0).getText mustEqual "Your claim date: 03/04/1950"
    }

    "failed to fill the form" in new WithBrowser with BrowserMatchers {
      browser.goTo("/about-you/claim-date")
      browser.submit("button[type='submit']")

      titleMustEqual("Your Claim Date - About you - the carer")
      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "This field is required"

      browser.fill("#dateOfClaim_year") `with` "1950"
      browser.submit("button[type='submit']")
      titleMustEqual("Your Claim Date - About you - the carer")

      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "Invalid value"
    }

    "navigate back to 'About Your Time Outside The UK'" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetailsEnablingTimeOutsideUK(browser)
      Formulate.yourContactDetails(browser)
      Formulate.timeOutsideUKNotLivingInUK(browser)
      titleMustEqual("Your Claim Date - About you - the carer")
      browser.click(".form-steps a")
      titleMustEqual(Messages("s2.g3") + " - About you - the carer")
    }

    "navigate back to 'Contact details'" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      browser.click(".form-steps a")
      titleMustEqual(Messages("s2.g2") + " - About you - the carer")
    }
  } section("integration", models.domain.AboutYou.id)
}