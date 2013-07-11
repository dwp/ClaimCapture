package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
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
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "contain error on missing separation date when separated" in new WithBrowser {
      browser.goTo("/yourPartner/moreAboutYourPartner")
      browser.click("#dateStartedLivingTogether_day option[value='3']")
      browser.click("#dateStartedLivingTogether_month option[value='4']")
      browser.fill("#dateStartedLivingTogether_year") `with` "1950"
      browser.click("#separatedFromPartner_yes]")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate back to Your Partner Personal Details" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      browser.click("#backButton")
      browser.title mustEqual "Contact Details - Your Partner"
    }
    
    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerSeparated(browser)
      browser.title mustEqual "Person You Care For - Your Partner"
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
  } section "integration"
}