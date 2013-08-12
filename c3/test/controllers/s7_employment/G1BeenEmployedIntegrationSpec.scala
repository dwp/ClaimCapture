package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G1BeenEmployedIntegrationSpec extends Specification with Tags {
  sequential

  "Been Employed" should {
    "present, having indicated that the carer has been employed" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")
      titleMustEqual("Your employment history - Employment History")
    }

   """be bypassed and go onto "other money" having indicated that "employment" is not required.""" in new WithBrowser with BrowserMatchers with NotEmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed")

      titleMustEqual("About Other Money - Other Money")
    }

    "start employment entry" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").click("#beenEmployed_yes").submit("button[type='submit']")
      titleMustEqual("Your job - Employment History")
    }

    "show 1 error upon submitting no mandatory data" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").submit("button[type='submit']")
      titleMustEqual("Your employment history - Employment History")
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    """continue to "completion" when there are no more "jobs" to submit.""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").click("#beenEmployed_no").submit("button[type='submit']")
      titleMustEqual("Completion - Employment History")
    }

    """go back to "education".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/been-employed").click("#backButton")
      titleMustEqual("Completion - About your education")
    }
  } section("integration", models.domain.Employed.id)
}