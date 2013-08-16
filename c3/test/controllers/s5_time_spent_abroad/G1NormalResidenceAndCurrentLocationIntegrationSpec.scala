package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}
import play.api.i18n.Messages

class G1NormalResidenceAndCurrentLocationIntegrationSpec extends Specification with Tags {
  "Normal residence and current location" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/normal-residence-and-current-location")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")
    }

    """give 2 errors when missing mandatory data:
       "Do you live in the UK?"
       "Are you in Great Britain now?".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/normal-residence-and-current-location")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    """give 2 errors when missing mandatory including optional to mandatory data:
       "Where do you normally live?"
       "Are you in Great Britain now?".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/normal-residence-and-current-location")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")

      browser.click("#liveInUK_answer_no")
      browser.submit("button[value='next']")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")
      browser.find("div[class=validation-summary] ol li").size mustEqual 2
    }

    """go back to "completed care you provide".""" in new WithBrowser with BrowserMatchers {
      Formulate.theirPersonalDetails(browser)
      browser.goTo("/time-spent-abroad/normal-residence-and-current-location")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual("Completion - About the care you provide")
    }
    
    // TODO tests for navigating to the next page on valid submission.
  } section("integration", models.domain.TimeSpentAbroad.id)
}