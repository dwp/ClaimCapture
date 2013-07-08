package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G1TheirPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Their Personal Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/careYouProvide/theirPersonalDetails")
      browser.title mustEqual "Their Personal Details - Care You Provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/theirPersonalDetails")
      browser.title mustEqual "Their Personal Details - Care You Provide"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 5
    }

    "navigate to next page on valid submission" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      browser.title mustEqual "Their Contact Details - Care You Provide"
    }

    """navigate back to "Completion - Your Partner" when they have had a partner/spouse at any time since the claim date""" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      browser.goTo("/careYouProvide/theirPersonalDetails")
      browser.click("#backButton")
      browser.title mustEqual "Completion - Your Partner"
    }
        
    """navigate back to "About You - Completed" when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYouNotHadPartnerSinceClaimDate(browser)
      browser.goTo("/careYouProvide/theirPersonalDetails")
      browser.click("#backButton")
      browser.title mustEqual "Completion - About You"
    }
    
    "contain the completed forms" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser {
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
      
      browser.find("#firstName").getValue mustEqual "John"
      browser.find("#surname").getValue mustEqual "Appleseed"
    }
  } section "integration"
}