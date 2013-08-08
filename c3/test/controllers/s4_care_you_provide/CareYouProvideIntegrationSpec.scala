package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """be presented""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      browser.goTo("/careYouProvide/completed")
      titleMustEqual("Completion - Care You Provide")
    }

    """navigate to Abroad""" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      Formulate.theirPersonalDetails(browser)
      titleMustEqual("Their Contact Details - Care You Provide")

      Formulate.theirContactDetails(browser)
      titleMustEqual("More About The Person You Care For - Care You Provide")

      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      titleMustEqual("Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerPersonalDetails(browser)
      titleMustEqual("Contact Details Of The Person Who Claimed Before - Care You Provide")

      Formulate.previousCarerContactDetails(browser)
      titleMustEqual("Representatives For The Person - Care You Provide")

      Formulate.representativesForThePerson(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      titleMustEqual("More about the care you provide - Care You Provide")

      browser.goTo("/careYouProvide/completed")
      titleMustEqual("Completion - Care You Provide")

      browser.find("button[type='submit']").getText shouldEqual "Continue to Abroad"

      browser.submit("button[type='submit']")
      titleMustEqual("Normal Residence and Current Location - Time Spent Abroad")
    }
  } section("integration",models.domain.CareYouProvide.id)
}