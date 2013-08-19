package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import play.api.i18n.Messages

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Your Partner Personal Details" should {
    "be presented if carer has partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse Details - About your partner/spouse")
    }

    "navigate to next section if carer has no partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      titleMustEqual("Employment - About you - the carer")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Details of the Person you care for - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse Details - About your partner/spouse")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact details - About your partner/spouse")
    }

    "navigate back to About you - the carer - Completed" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse Details - About your partner/spouse")

      browser.click("#backButton")
      titleMustEqual("Completion - About you - the carer")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.moreAboutYou(browser)
      titleMustEqual("Employment - About you - the carer")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact details - About your partner/spouse")
      findMustEqualSize("div[class=completed] ul li", 1)
    }
        
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.timeOutsideUKNotLivingInUK(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Contact details - About your partner/spouse")
      
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
  } section("integration", models.domain.YourPartner.id)
}