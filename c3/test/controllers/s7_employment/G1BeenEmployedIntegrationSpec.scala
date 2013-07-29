package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G1BeenEmployedIntegrationSpec extends Specification with Tags {
  sequential

  "Been Employed" should {
    "present, having indicated that the carer has been employed" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed")
      titleMustEqual("Your employment history - Employment")
    }

    """be bypassed and go onto "self employment" having not indicated that "employment" is required.""" in new WithBrowser {
      browser.goTo("/employment/beenEmployed")
      browser.title mustEqual "Self Employment - About Self Employment"
    }

    """be bypassed and go onto "self employment" having indicated that "employment" is not required.""" in new WithBrowser with BrowserMatchers with NotEmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed")
      titleMustEqual("Self Employment - About Self Employment")
    }

    "start employment entry" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed").click("#beenEmployed_yes").submit("button[type='submit']")
      titleMustEqual("Job Details - Employment")
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed").submit("button[type='submit']")
      titleMustEqual("Your employment history - Employment")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    """continue to "completion" when there are no more "jobs" to submit.""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed").click("#beenEmployed_no").submit("button[type='submit']")
      titleMustEqual("Completion - Employment")
    }

    """go back to "education".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed").click("#backButton")
      titleMustEqual("Completion - Education")
    }
  } section "integration"
}