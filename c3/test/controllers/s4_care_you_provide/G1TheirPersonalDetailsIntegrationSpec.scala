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

    """navigate back to "Partner details - About your partner" when they have had a partner/spouse at any time since the claim date""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      browser.goTo("/care-you-provide/their-personal-details")
      Formulate.clickBackButton(browser)
      titleMustEqual(G1YourPartnerPersonalDetailsPage.title)
    }

    
    "be pre-populated if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.employment(browser)
      Formulate.yourPartnerPersonalDetails(browser)
      browser.submit("button[type='submit']")

      titleMustEqual("Details of the person you care for - About the care you provide")
      findMustEqualValue("#firstName","John")
      findMustEqualValue("#surname", "Appleseed")
    }

    "fields must not be visible if user answered yes to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.yourPartnerPersonalDetails(browser)

      titleMustEqual("Details of the person you care for - About the care you provide")
      findMustEqualSize("#careYouProvideWrap", 1)
      browser.find("#careYouProvideWrap").getAttribute("style") shouldEqual "display: none;"
    }

    "fields must be visible if user answered no to claiming for partner/spouse in yourPartner/personYouCareFor section" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.yourPartnerPersonalDetailsPartnerPersonYouCareForNo(browser)

      titleMustEqual("Details of the person you care for - About the care you provide")
      findMustEqualSize("#careYouProvideWrap", 1)
      browser.find("#careYouProvideWrap").getAttribute("style") shouldEqual null
    }

  } section("integration", models.domain.CareYouProvide.id)
}