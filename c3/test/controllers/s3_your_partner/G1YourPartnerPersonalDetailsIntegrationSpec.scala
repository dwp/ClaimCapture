package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import utils.pageobjects.s4_care_you_provide.G1TheirPersonalDetailsPage

class G1YourPartnerPersonalDetailsIntegrationSpec extends Specification with Tags {

  "Your Partner Personal Details" should {
    "be presented if carer has partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual(G1TheirPersonalDetailsPage.title)
    }

    "navigate back to Employment - About you - the carer" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)

      titleMustEqual("Partner details - About your partner")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner details - About your partner")

      browser.click("#backButton")
      titleMustEqual("Payments from abroad and working abroad - About you - the carer")
    }

    "navigate back to About your partner/spouse - Partner/Spouse details" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      titleMustEqual("Partner details - About your partner")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Details of the person you care for - About the care you provide")
      Formulate.clickBackButton(browser)
      titleMustEqual("Partner details - About your partner")
    }

    "nationality should not be visible when the carer is British" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 0
    }

    "nationality should be visible when the carer is not british and married" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishMarried(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 1
    }

    "nationality should be visible when the carer is not british and Living with partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishWithPartner(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 1
    }

    "nationality should not be visible when the carer is not british and neither married or living with partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidencyNotBritishSingle(browser)
      browser.goTo("/your-partner/personal-details")
      browser.click("#hadPartnerSinceClaimDate_yes")
      browser.find("#nationality").size shouldEqual 0
    }

  } section("integration", models.domain.YourPartner.id)
}