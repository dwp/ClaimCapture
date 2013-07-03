package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G4ClaimDateIntegrationSpec extends Specification with Tags {

  "Claim Date" should {
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/claimDate")
      browser.title mustEqual "Claim Date - About You"
    }

    "contain 2 completed forms" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)

      browser.title mustEqual "Claim Date - About You"
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "fill date" in new WithBrowser {
      FormHelper.fillClaimDate(browser)

      browser.title mustEqual "More About You - About You"
      browser.find("div[class=completed] ul li h3").get(0).getText mustEqual "Your claim date: 03/04/1950"
    }

    "failed to fill the form" in new WithBrowser {
      browser.goTo("/aboutyou/claimDate")
      browser.submit("button[type='submit']")

      browser.title mustEqual "Claim Date - About You"
      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "This field is required"

      browser.fill("#dateOfClaim_year") `with` "1950"
      browser.submit("button[type='submit']")

      browser.title mustEqual "Claim Date - About You"
      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "Invalid value"
    }

    "navigate back to Time Outside UK" in new WithBrowser {
      FormHelper.fillYourDetailsEnablingTimeOutsideUK(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTimeOutsideUK(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Time Outside UK - About You"
    }

    "navigate back to Contact Details" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      browser.click(".form-steps a")
      browser.title mustEqual "Contact Details - About You"
    }
  } section "integration"
}