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
      browser.click(".form-steps a")
      browser.title mustEqual "Completion - Your Partner"
    }
        
    """navigate back to "About You - Completed" when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYouNotHadPartnerSinceClaimDate(browser)
      browser.goTo("/careYouProvide/theirPersonalDetails")
      browser.click(".form-steps a")
      browser.title mustEqual "Completion - About You"
    }
    
    "contain the completed forms" in new WithBrowser {
      FormHelper.fillTheirPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}