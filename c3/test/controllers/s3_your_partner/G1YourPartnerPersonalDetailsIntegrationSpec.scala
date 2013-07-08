package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.FormHelper
import org.specs2.execute.PendingUntilFixed

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags with PendingUntilFixed {

  "Your Partner Personal Details" should {
    "be presented if carer has partner" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      browser.goTo("/yourPartner/personalDetails")
      browser.title mustEqual "Personal Details - Your Partner"
    }

    "navigate to next section if carer has no partner"  in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYouNotHadPartnerSinceClaimDate(browser)
      browser.goTo("/yourPartner/personalDetails")
      browser.title mustNotEqual "Personal Details - Your Partner"
    }

    "contain errors on invalid submission" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      browser.goTo("/yourPartner/personalDetails")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 5
    }

    "navigate to next page on valid submission" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      FormHelper.fillYourPartnerPersonalDetails(browser)
      browser.title mustEqual "Contact Details - Your Partner"
    }

    "navigate back to About You - Completed" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      browser.goTo("/yourPartner/personalDetails")
      browser.click("#backButton")
      browser.title mustEqual "Completion - About You"
    }

    "contain the completed forms" in new WithBrowser {
      FormHelper.fillClaimDate(browser)
      FormHelper.fillMoreAboutYou(browser)
      FormHelper.fillYourPartnerPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
  } section "integration"
}