package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Your Partner Personal Details" should {
    "be presented if carer has partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")

      browser.goTo("/yourPartner/personalDetails")
      titleMustEqual("Partner/Spouse Details - About Your Partner/Spouse")
    }

    "navigate to next section if carer has no partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      titleMustEqual("Employment - About You")

      browser.goTo("/yourPartner/personalDetails")
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")

      browser.goTo("/yourPartner/personalDetails")
      titleMustEqual("Partner/Spouse Details - About Your Partner/Spouse")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 5
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact Details - About your partner/spouse")
    }

    "navigate back to About You - Completed" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")

      browser.goTo("/yourPartner/personalDetails")
      titleMustEqual("Partner/Spouse Details - About Your Partner/Spouse")

      browser.click("#backButton")
      titleMustEqual("Completion - About You")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About You")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact Details - About your partner/spouse")
      findMustEqualSize("div[class=completed] ul li", 1)
    }
        
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.timeOutsideUKNotLivingInUK(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.propertyAndRent(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact Details - About your partner/spouse")
      
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  } section("integration", models.domain.YourPartner.id)
}