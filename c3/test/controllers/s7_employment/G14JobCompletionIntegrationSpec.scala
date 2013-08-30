package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G14JobCompletionIntegrationSpec extends Specification with Tags {
  val jobID = "dummyJobID"

  "Job completion - Integration" should {
    "present" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo(s"/employment/job-completion/$jobID")
      titleMustEqual("Job Completion - Employment History")
    }

    """progress back to start i.e. "employment history".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim()

      goTo("/employment/been-employed")

      goTo(s"/employment/job-completion/$jobID")
      titleMustEqual("Job Completion - Employment History")

      next
      titleMustEqual("Your employment history - Employment History")
    }

    """go back to "Person you care for expenses".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      /* Required data to get to "end" pages */
      goTo(s"/employment/about-expenses/$jobID")
      click("#payForAnythingNecessary_yes")
      click("#payAnyoneToLookAfterChildren_yes")
      click("#payAnyoneToLookAfterPerson_yes")
      next
      titleMustEqual("Necessary expenses to do your job - Employment History")

      /* The page we wish to go back to */
      goTo(s"/employment/person-you-care-for-expenses/$jobID")
      titleMustEqual("Expenses related to the person you care for, while you are at work - Employment History")
      fill("#whoDoYouPay") `with` "someone"
      click("#relationToPersonYouCare option[value='brother']")
      click("#relationToYou option[value='sister']")

      next
      titleMustEqual("Job Completion - Employment History")

      click("#backButton")
      titleMustEqual("Expenses related to the person you care for, while you are at work - Employment History")
    }
  } section("integration", models.domain.Employed.id)
}