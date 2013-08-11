package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G1TheirPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Their Personal Details" should {
    "be presented" in new WithBrowser {
      browser.goTo("/care-you-provide/their-personal-details")
      browser.title mustEqual "Details of the person you care for - About the care you provide"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/care-you-provide/their-personal-details")
      browser.title mustEqual "Details of the person you care for - About the care you provide"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 5
    }

    "navigate to next page on valid submission" in new WithBrowser {
      Formulate.theirPersonalDetails(browser)
      browser.title mustEqual "Contact details of the person you care for - About the care you provide"
    }

    """navigate back to "Completion - About Your Partner/Spouse" when they have had a partner/spouse at any time since the claim date""" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      browser.goTo("/care-you-provide/their-personal-details")
      browser.click("#backButton")
      browser.title mustEqual "Completion - About Your Partner/Spouse"
    }
        
    """navigate back to "About You - Completed" when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      browser.goTo("/care-you-provide/their-personal-details")
      browser.click("#backButton")
      browser.title mustEqual "Completion - About You"
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.theirPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.timeOutsideUKNotLivingInUK(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerNotSeparated(browser)
      Formulate.personYouCareFor(browser)
      browser.submit("button[type='submit']")

      titleMustEqual("Details of the person you care for - About the care you provide")
      findMustEqualValue("#firstName","John")
      browser.find("#surname").getValue mustEqual "Appleseed"
    }
  } section("integration", models.domain.CareYouProvide.id)
}