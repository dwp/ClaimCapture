package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

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
      Formulate.theirPersonalDetails(browser)
      Formulate.theirContactDetails(browser)
      Formulate.moreAboutThePersonWithClaimedAllowanceBefore(browser)
      Formulate.previousCarerPersonalDetails(browser)
      Formulate.previousCarerContactDetails(browser)
      Formulate.representativesForThePerson(browser)
      Formulate.moreAboutTheCareWithNotSpent35HoursCaringBeforeClaim(browser)
      browser.goTo("/careYouProvide/completed")
      titleMustEqual("Completion - Care You Provide")(Duration(10, TimeUnit.MINUTES))

      browser.find("button[type='submit']").getText shouldEqual "Continue to Abroad"

      browser.submit("button[type='submit']")
      titleMustEqual("Normal Residence and Current Location - Time Spent Abroad")(Duration(10, TimeUnit.MINUTES))

    }

  } section "integration"
}