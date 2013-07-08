package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper
import controllers.WithBrowserAndMatchers

class G2TheirContactDetailsIntegrationSpec extends Specification with Tags {

  "Their Contact Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/theirContactDetails")
      browser.title mustEqual "Their Contact Details - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/theirContactDetails")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "be prepopulated if they live at same address" in new WithBrowser {
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTheirPersonalDetails(browser)
      browser.title mustEqual "Their Contact Details - Care You Provide"
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }

    "be blank if they live at different address" in new WithBrowser {
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTheirPersonalDetailsNotLiveAtSameAddress(browser)
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "be blank if they live at same address but did not enter one" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "navigate back to Their Personal Details" in new WithBrowser {
      browser.goTo("/careYouProvide/theirContactDetails")
      browser.click("#backButton")
      browser.title mustEqual "Their Personal Details - Care You Provide"
    }

    "navigate to next page on valid submission" in new WithBrowser {
      FormHelper.fillTheirContactDetails(browser)
      browser.title mustEqual "More About The Person You Care For - Care You Provide"
    }

    "overwrite cached contact details after going back and changing answer to living at same address" in new WithBrowser {
      FormHelper.fillTheirContactDetails(browser)
      browser.click("#backButton")
      browser.find("#address_lineOne").getValue mustEqual "Their Address"
      browser.click("#backButton")
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTheirPersonalDetails(browser)

      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowserAndMatchers {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillTimeOutsideUK(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      FormHelper.fillEmployment(browser)
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartnerNotSeparated(browser)
      FormHelper.fillPersonYouCareFor(browser)
      browser.submit("button[type='submit']")
      browser.submit("button[type='submit']")

      findMustEqualValue("#address_lineOne", "My Address")
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  } section "integration"
}