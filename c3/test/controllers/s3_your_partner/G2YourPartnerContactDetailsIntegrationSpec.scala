package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G2YourPartnerContactDetailsIntegrationSpec extends Specification with Tags {



  "Your Partner Contact Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.title mustEqual "Contact Details - Your Partner"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.fill("#postcode") `with` "INVALD"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "be prepopulated if they live at same address" in new WithBrowser {
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillYourPartnerPersonalDetails(browser)
      browser.title mustEqual "Contact Details - Your Partner"
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }

    "be blank if they live at different address" in new WithBrowser {
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillYourPartnerPersonalDetailsNotLiveAtSameAddress(browser)
      browser.title mustEqual "Contact Details - Your Partner"
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "be blank if they live at same address but did not enter one" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser {
      browser.goTo("/yourPartner/contactDetails")
      browser.click("#backButton")
      browser.title mustEqual "Personal Details - Your Partner"
    }

    "navigate to next page on valid submission" in new WithBrowser {
      FormHelper.fillYourPartnerContactDetails(browser)
      browser.title mustEqual "More About Your Partner - Your Partner"
    }

    "overwrite cached contact details after going back and changing answer to living at same address" in new WithBrowser {
      FormHelper.fillYourPartnerContactDetails(browser)
      browser.click("#backButton")
      browser.find("#address_lineOne").getValue mustEqual "Partner Address"
      browser.click("#backButton")
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillYourPartnerPersonalDetails(browser)

      browser.title mustEqual "Contact Details - Your Partner"
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  }

}
