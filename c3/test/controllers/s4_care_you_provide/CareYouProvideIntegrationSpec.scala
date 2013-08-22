package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """be presented""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")
    }

    """navigate to Abroad""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue to time abroad"

      browser.submit("button[type='submit']")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")
    }
  } section("integration", models.domain.CareYouProvide.id)
}