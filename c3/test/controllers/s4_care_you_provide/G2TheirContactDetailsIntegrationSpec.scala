package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2TheirContactDetailsIntegrationSpec extends Specification with Tags {

  "Their Contact Details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/their-contact-details")
      titleMustEqual("Contact details of the person you care for - About the care you provide")
    }

    "contain errors on empty submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/their-contact-details")
      browser.submit("button[type='submit']")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
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

      browser.goTo("/care-you-provide/their-contact-details")
      browser.click("#backButton")
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.theirContactDetails(browser)
      titleMustEqual("More about the care you provide - About the care you provide")
    }
  } section("integration", models.domain.CareYouProvide.id)
}