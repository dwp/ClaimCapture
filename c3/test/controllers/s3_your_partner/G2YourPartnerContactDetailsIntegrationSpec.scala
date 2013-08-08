package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2YourPartnerContactDetailsIntegrationSpec extends Specification with Tags {
  "Your Partner Contact Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.title mustEqual "Contact Details - About your partner/spouse"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.fill("#postcode") `with` "INVALD"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be prepopulated if they live at same address" in new WithBrowser {
      Formulate.yourContactDetails(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      browser.title mustEqual "Contact Details - About your partner/spouse"
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }

    "be blank if they live at different address" in new WithBrowser {
      Formulate.yourContactDetails(browser)
      Formulate.yourPartnerPersonalDetailsNotLiveAtSameAddress(browser)
      browser.title mustEqual "Contact Details - About your partner/spouse"
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "be blank if they live at same address but did not enter one" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact Details - About your partner/spouse")

      browser.goTo("/yourPartner/contactDetails")
      browser.click("#backButton")
      titleMustEqual("Partner/Spouse Details - About Your Partner/Spouse")
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.yourPartnerContactDetails(browser)
      browser.title mustEqual "More About Your Partner/Spouse - About Your Partner/Spouse"
    }

    "overwrite cached contact details after going back and changing answer to living at same address" in new WithBrowser with BrowserMatchers {
      Formulate.yourPartnerContactDetails(browser)
      titleMustEqual("More About Your Partner/Spouse - About Your Partner/Spouse")
      browser.click("#backButton")
      titleMustEqual("Contact Details - About your partner/spouse")
      browser.find("#address_lineOne").getValue mustEqual Formulate.partnerAddress
      browser.find("#postcode").getValue mustEqual Formulate.partnerPostcode

      Formulate.yourContactDetails(browser)
      Formulate.yourPartnerPersonalDetails(browser)

      titleMustEqual("Contact Details - About your partner/spouse")
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  }  section("integration",models.domain.YourPartner.id)
}
