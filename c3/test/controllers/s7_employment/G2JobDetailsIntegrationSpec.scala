package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G2JobDetailsIntegrationSpec extends Specification with Tags {
  "Details about your job" should {
    "present" in new WithBrowser {
      browser.goTo("/employment/jobDetails")
      browser.title mustEqual "Job Details - Employment"
    }

    "show 2 errors upon submitting no mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/jobDetails").submit("button[type='submit']")
      titleMustEqual("Job Details - Employment")
      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    // ...

    """go back to "been employed?".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/beenEmployed").click("#beenEmployed_yes").submit("button[type='submit']")
      browser.goTo("/employment/jobDetails").click("#backButton")
      titleMustEqual("Your employment history - Employment")
    }
  } section "integration"
}