package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G14JobCompletionIntegrationSpec extends Specification with Tags {
  val jobID = "dummyJobID"

  "Job completion - Integration" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo(s"/employment/job-completion/$jobID")
      titleMustEqual("Job Completion - Employment History")
    }

    """progress back to start i.e. "employment history".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo(s"/employment/job-completion/$jobID")
      titleMustEqual("Job Completion - Employment History")

      browser.submit("button[type='submit']")
      titleMustEqual("Your employment history - Employment History")
    }

    """go back to "Care provider's contact Details".""" in new WithBrowser with BrowserMatchers {
      /* Required data to get to "end" pages */
      browser.goTo(s"/employment/about-expenses/$jobID")
      browser.click("#payForAnythingNecessary_yes")
      browser.click("#payAnyoneToLookAfterChildren_yes")
      browser.click("#payAnyoneToLookAfterPerson_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Necessary expenses to do your job - Employment History")

      /* The page we wish to go back to */
      browser.goTo(s"/employment/care-provider/$jobID")
      titleMustEqual("Care provider's contact details - Employment History")

      browser.submit("button[type='submit']")
      titleMustEqual("Job Completion - Employment History")

      browser.click("#backButton")
      titleMustEqual("Care provider's contact details - Employment History")
    }
  } section("integration", models.domain.Employed.id)
}