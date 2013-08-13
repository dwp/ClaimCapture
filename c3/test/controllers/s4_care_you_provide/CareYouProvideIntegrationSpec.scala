package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import play.api.i18n.Messages

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """be presented""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the Person you care for - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")
    }

    """navigate to Abroad""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual(Messages("s2.g5") + " - About you - the carer")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Contact details of the Person you care for - About the care you provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("Relationship and other claims - About the care you provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details of Previous or Existing Carer - About the care you provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact details of previous or existing carer - About the care you provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives for the Person you care for - About the care you provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - About the care you provide")

      browser.goTo("/care-you-provide/completed")
      titleMustEqual("Completion - About the care you provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue to Abroad"

      browser.submit("button[type='submit']")
      titleMustEqual(Messages("s5.g1") + " - Time Spent Abroad")
    }
  } section("integration", models.domain.CareYouProvide.id)
}