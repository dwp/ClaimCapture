package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G14JobCompletionIntegrationSpec extends Specification with Tags {
  val jobID = "dummyJobID"

  "Job completion" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo(s"/employment/jobCompletion/$jobID")
      titleMustEqual("Job Completion - Employment")
    }

    /*"""progress to "employment completion".""" in new WithBrowser with BrowserMatchers {
      browser.goTo(s"/employment/jobCompletion/$jobID")
      titleMustEqual("Job Completion - Employment")
      browser.submit("button[type='submit']")
      titleMustEqual("Completion - Employment")
    }*/

    """go back to "Care provider’s contact Details".""" in new WithBrowser with BrowserMatchers {
      /* Required data to get to "end" pages */
      browser.goTo(s"/employment/aboutExpenses/$jobID")
      browser.click("#payForAnythingNecessary_yes")
      browser.click("#payAnyoneToLookAfterChildren_yes")
      browser.click("#payAnyoneToLookAfterPerson_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Necessary expenses to do your job - Employment")

      /* The page we wish to go back to */
      browser.goTo(s"/employment/careProvider/$jobID")
      titleMustEqual("Care provider’s contact Details - Employment")

      browser.submit("button[type='submit']")
      titleMustEqual("Job Completion - Employment")

      browser.click("#backButton")
      titleMustEqual("Care provider’s contact Details - Employment")
    }
  } section "integration"
}