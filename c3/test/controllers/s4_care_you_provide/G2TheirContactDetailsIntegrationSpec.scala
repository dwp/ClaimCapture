package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2TheirContactDetailsIntegrationSpec extends Specification with Tags {

  "Their Contact Details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/careYouProvide/theirContactDetails")
      titleMustEqual("Contact details of the person you care for - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/careYouProvide/theirContactDetails")
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "be prepopulated if they live at same address" in new WithBrowser with BrowserMatchers {
      Formulate.yourContactDetails(browser)
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")
      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }

    "be blank if they live at different address" in new WithBrowser with BrowserMatchers {
      Formulate.yourContactDetails(browser)
      Formulate.theirPersonalDetailsNotLiveAtSameAddress(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")
      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "be blank if they live at same address but did not enter one" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      browser.find("#address_lineOne").getValue mustEqual ""
      browser.find("#postcode").getValue mustEqual ""
    }

    "navigate back to Their Personal Details" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      browser.goTo("/careYouProvide/theirContactDetails")
      browser.click("#backButton")
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")
    }

    "overwrite cached contact details after going back and changing answer to living at same address" in new WithBrowser {
      Formulate.theirContactDetails(browser)
      browser.click("#backButton")
      browser.find("#address_lineOne").getValue mustEqual "Their Address"
      browser.click("#backButton")
      Formulate.yourContactDetails(browser)
      Formulate.theirPersonalDetails(browser)

      browser.find("#address_lineOne").getValue mustEqual "My Address"
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
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
      browser.submit("button[type='submit']")

      findMustEqualValue("#address_lineOne", "My Address")
      browser.find("#postcode").getValue mustEqual "SE1 6EH"
    }
    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section but not at same address" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.timeOutsideUKNotLivingInUK(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)
      Formulate.employment(browser)
      Formulate.yourPartnerPersonalDetailsNotLiveAtSameAddress(browser)
      Formulate.yourPartnerContactDetails(browser)
      Formulate.moreAboutYourPartnerNotSeparated(browser)
      Formulate.personYouCareFor(browser)
      Formulate.yourPartnerCompleted(browser)
      browser.submit("button[type='submit']") // S4G1 go to S4G2 without changing any of the details onscreen.

      findMustEqualValue("#address_lineOne", Formulate.partnerAddress)
      browser.find("#postcode").getValue mustEqual Formulate.partnerPostcode
    }
  } section("integration", models.domain.CareYouProvide.id)
}