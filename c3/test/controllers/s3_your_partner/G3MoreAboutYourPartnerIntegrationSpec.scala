package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper
import org.specs2.execute.PendingUntilFixed

class G3MoreAboutYourPartnerIntegrationSpec extends Specification with Tags with PendingUntilFixed {

  "More About Your Partner" should {
    "be presented" in new WithBrowser {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.title mustEqual "More About Your Partner - Your Partner"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.click("#backButton")
      browser.title mustEqual "Contact Details - Your Partner"
    }
        
    
    
    
    
    
    
    "contain the completed forms" in new WithBrowser {
      FormHelper.fillMoreAboutYourPartner(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }.pendingUntilFixed("Need S3G4 view to exist and for the controller to be wired up")
    

/*







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
    }*/
  }
}