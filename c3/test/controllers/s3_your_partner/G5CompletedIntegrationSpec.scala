package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper

class G5CompletedIntegrationSpec extends Specification with Tags {

  "Your Partner" should {
    "be presented" in new WithBrowser {
      browser.goTo("/yourPartner/completed")
      browser.title mustEqual "Completion - Your Partner"
    }

    """be submitted to "care you provide" page.""" in new WithBrowser {
      browser.goTo("/yourPartner/completed")
      browser.submit("button[type='submit']")
      browser.title mustEqual "Their Personal Details - Care You Provide"
    }
    
    "contain the completed forms" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartnerSeparated(browser)
      FormHelper.fillPersonYouCareFor(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 4
    }
  } section "integration"
}