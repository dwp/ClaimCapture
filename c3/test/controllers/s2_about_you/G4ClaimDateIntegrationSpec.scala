package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import controllers.{WithBrowserAndMatchers, FormHelper}

class G4ClaimDateIntegrationSpec extends Specification with Tags {

  "Claim Date" should {
    "be presented" in new WithBrowserAndMatchers {
      browser.goTo("/aboutyou/claimDate")
      titleMustEqual("Claim Date - About You")
    }

    "contain 2 completed forms" in new WithBrowserAndMatchers {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)

      titleMustEqual("Claim Date - About You")
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }

    "fill date" in new WithBrowserAndMatchers {
      FormHelper.fillClaimDate(browser)

      titleMustEqual("More About You - About You")
      browser.find("div[class=completed] ul li h3").get(0).getText mustEqual "Your claim date: 03/04/1950"
    }

    "failed to fill the form" in new WithBrowserAndMatchers {
      browser.goTo("/aboutyou/claimDate")
      browser.submit("button[type='submit']")

      titleMustEqual("Claim Date - About You")
      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "This field is required"

      browser.fill("#dateOfClaim_year") `with` "1950"
      browser.submit("button[type='submit']")

      titleMustEqual("Claim Date - About You")
      browser.find("p[class=error]").size() must beGreaterThan(0)
      browser.find("p[class=error]").get(0).getText mustEqual "Invalid value"
    }

    "navigate back to Time Outside UK" in new WithBrowserAndMatchers {
      FormHelper.fillYourDetailsEnablingTimeOutsideUK(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTimeOutsideUK(browser)
      browser.click(".form-steps a")
      titleMustEqual("Time Outside UK - About You")
    }

    "navigate back to Contact Details" in new WithBrowserAndMatchers {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      browser.click(".form-steps a")
      titleMustEqual("Contact Details - About You")
    }
  } section "integration"
}