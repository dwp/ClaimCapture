package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G1BeenEmployedIntegrationSpec extends Specification with Tags {
  "Been Employed" should {
    "present" in new WithBrowser {
      browser.goTo("/employment/beenEmployed")
      browser.title mustEqual "Your employment history - Employment"
    }

    "start employment entry" in new WithBrowser {
      browser.goTo("/employment/beenEmployed").click("#beenEmployed_yes").submit("button[type='submit']")
      browser.title mustEqual "Job Details - Employment"
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/beenEmployed").submit("button[type='submit']")
      titleMustEqual("Your employment history - Employment")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "continue to self employed" in new WithBrowser {
      skipped("""Awaiting development of "self employed".""")

      browser.goTo("/employment/beenEmployed").click("#beenEmployed_no").submit("button[type='submit']")
      browser.title mustEqual "START - Self Employment"
    }

    """go back to "education".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/beenEmployed").click("#backButton")
      titleMustEqual("Completion - Education")
    }
  } section "integration"
}