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
      Formulate.moreAboutYou(browser)
      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse details - About your partner/spouse")
    }

    "navigate to next section if carer has no partner" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYouNotHadPartnerSinceClaimDate(browser)
      titleMustEqual("Details of the person you care for - About the care you provide")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Details of the person you care for - About the care you provide")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse details - About your partner/spouse")
      browser.submit("button[type='submit']")

      browser.find("div[class=validation-summary] ol li").size mustEqual 6
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual(G1TheirPersonalDetailsPage.title)
    }

    "navigate back to Employment - About you - the carer" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)

      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      browser.goTo("/your-partner/personal-details")
      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      browser.click("#backButton")
      titleMustEqual("More about you - About you - the carer")
    }

    "navigate back to About your partner/spouse - Partner/Spouse details" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      titleMustEqual("Partner/Spouse details - About your partner/spouse")

      Formulate.yourPartnerPersonalDetails(browser)
      titleMustEqual("Details of the person you care for - About the care you provide")
      Formulate.clickBackButton(browser)
      titleMustEqual("Partner/Spouse details - About your partner/spouse")
    }
  } section("integration", models.domain.YourPartner.id)
}