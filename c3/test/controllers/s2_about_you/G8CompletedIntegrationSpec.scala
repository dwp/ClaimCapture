package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import org.specs2.execute.PendingUntilFixed
import controllers.FormHelper

class G8CompletedIntegrationSpec extends Specification with Tags {

  "About You" should {
    "be presented" in new WithBrowser {
      browser.goTo("/aboutyou/completed")
      browser.title mustEqual "Completion - About You"
    }
    
    """show the text "Continue to Partner / Spouse" on the submit button when next section is "Your Partner"""" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      FormHelper.fillEmployment(browser)
      FormHelper.fillPropertyAndRent(browser)
      
      pending("TODO test submit button text")
      browser.find("#submit").getValue mustEqual "Continue to Partner / Spouse"
    }
    
    """show the text "Continue to Care You Provide" on the submit button when next section is "Care You Provide"""" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYouNotHadPartnerSinceClaimDate(browser)
      FormHelper.fillEmployment(browser)
      FormHelper.fillPropertyAndRent(browser)
      
      pending("TODO test submit button text")
      browser.find("#submit").getValue mustEqual "Continue to Care You Provide"
    }

    """be submitted to "Personal Details - Your Partner" page when they have had a partner/spouse at any time since the claim date""" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      FormHelper.fillEmployment(browser)
      FormHelper.fillPropertyAndRent(browser)
      
      browser.submit("button[type='submit']")
      
      browser.title mustEqual "Personal Details - Your Partner"  
    }

    """be submitted to "Their Personal Details - Care You Provide" page when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser {
      FormHelper.fillYourDetails(browser)
      FormHelper.fillYourContactDetails(browser)
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYouNotHadPartnerSinceClaimDate(browser)
      FormHelper.fillEmployment(browser)
      FormHelper.fillPropertyAndRent(browser)
      
      browser.submit("button[type='submit']")
      
      browser.title mustEqual "Their Personal Details - Care You Provide"
    }
    
  } section "integration"
}