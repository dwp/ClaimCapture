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
      browser.click("#backButton")
      browser.title mustEqual "Contact Details - Your Partner"
    }
    
    "navigate to next page on valid submission" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartner(browser)
      browser.title mustEqual "Date of Separation - Your Partner"
    }.pendingUntilFixed("Needs S3G4 view to exist and for the controller to be wired up")
    
    "contain the completed forms" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartner(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }.pendingUntilFixed("Needs S3G4 view to exist and for the controller to be wired up")
    
    "navigate to Date of Separation when submitting with separated positive" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartner(browser)
      browser.title mustEqual "Date Of Separation - Your Partner"
    }.pendingUntilFixed("Needs S3G4 view to exist and for the controller to be wired up")

    "navigate to Date of Separation when submitting with separated negative" in new WithBrowser {
      FormHelper.fillYourPartnerPersonalDetails(browser)
      FormHelper.fillYourPartnerContactDetails(browser)
      FormHelper.fillMoreAboutYourPartnerNotSeparated(browser)
      browser.title mustEqual "Person Tou Care For - Your Partner"
    }.pendingUntilFixed("Needs S3G4 Controller to redirect to S3G5 and for S3G5 view to exist and for the controller to be wired up")
  }
}