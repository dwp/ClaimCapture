package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage

class G1TheirPersonalDetailsIntegrationSpec extends Specification with Tags {
  "Their Personal Details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/their-personal-details")
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/care-you-provide/their-personal-details")
      browser.title mustEqual "Details of the person you care for - About the care you provide"
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 7
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")
    }

    """navigate back to "Partner/Spouse details - About your partner/spouse" when they have had a partner/spouse at any time since the claim date""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      browser.goTo("/care-you-provide/their-personal-details")
      Formulate.clickBackButton(browser)
      titleMustEqual(G1YourPartnerPersonalDetailsPage.title)
    }
        
    """navigate back to "More about you - About you - the carer" when they have NOT had a partner/spouse at any time since the claim date""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      browser.goTo("/care-you-provide/their-personal-details")
      browser.click("#backButton")
      titleMustEqual("More about you - About you - the carer")
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.theirPersonalDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }
    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      browser.submit("button[type='submit']")

      titleMustEqual("Details of the person you care for - About the care you provide")
      findMustEqualValue("#firstName","John")
      findMustEqualValue("#surname", "Appleseed")
    }
  } section("integration", models.domain.CareYouProvide.id)
}