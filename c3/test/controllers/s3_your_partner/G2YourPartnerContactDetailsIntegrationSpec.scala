package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2YourPartnerContactDetailsIntegrationSpec extends Specification with Tags {
  "Your Partner Contact details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/your-partner/contact-details")
      browser.title mustEqual "Contact details - About your partner/spouse"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/your-partner/contact-details")
      browser.fill("#postcode") `with` "INVALD"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact details - About your partner/spouse")

      browser.goTo("/your-partner/contact-details")
      browser.click("#backButton")
      titleMustEqual("Partner/Spouse Details - About your partner/spouse")
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.yourPartnerContactDetails(browser)
      browser.title mustEqual "More about your Partner/Spouse - About your partner/spouse"
    }

    "overwrite cached Contact details after going back and changing answer to living at same address" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More about your Partner/Spouse - About your partner/spouse")
      browser.click("#backButton")
      titleMustEqual("Contact details - About your partner/spouse")
      browser.find("#address_lineOne").getValue mustEqual Formulate.partnerAddress
      browser.find("#postcode").getValue mustEqual Formulate.partnerPostcode

      Formulate.yourContactDetails(browser)
      Formulate.yourPartnerPersonalDetails(browser)

      titleMustEqual("Contact details - About your partner/spouse")
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  }  section("integration", models.domain.YourPartner.id)
}