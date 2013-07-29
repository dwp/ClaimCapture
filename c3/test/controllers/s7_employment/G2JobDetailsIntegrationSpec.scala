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

    "accept only mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/jobDetails")
      browser.fill("#employerName") `with` "Toys r not Us"
      browser.click("#finishedThisJob_no")

      browser.submit("button[type='submit']")
      titleMustEqual("Employer contact details - Employment")
    }

    "accept all data" in new WithBrowser with EmploymentFiller {
      jobDetails
    }

    """go back to "been employed?".""" in new WithBrowser with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim

      browser.goTo("/employment/beenEmployed").click("#beenEmployed_yes").submit("button[type='submit']")
      browser.goTo("/employment/jobDetails").click("#backButton")
      titleMustEqual("Your employment history - Employment")
    }

    "begin twice, kicking off 2 jobs and choose to start editing the first job" in new WithBrowser with EmploymentFiller {
      skipped("Usual rubbish timing issues - works fine when run on its own")

      jobDetails
      jobDetails

      browser.goTo("/employment/beenEmployed")
      browser.$("#jobs table tbody tr").size() shouldEqual 2

      browser.findFirst("input[value='Edit']").click()
      titleMustEqual("Job Details - Employment")
    }
  } section "integration"

  trait EmploymentFiller extends BrowserMatchers {
    this: WithBrowser[_] =>

    def jobDetails(): Unit = {
      browser.goTo("/employment/jobDetails")

      browser.fill("#employerName") `with` "Toys r not Us"

      browser.click("#jobStartDate_day option[value='1']")
      browser.click("#jobStartDate_month option[value='1']")
      browser.fill("#jobStartDate_year") `with` "2000"

      browser.click("#finishedThisJob_yes")

      browser.click("#lastWorkDate_day option[value='1']")
      browser.click("#lastWorkDate_month option[value='1']")
      browser.fill("#lastWorkDate_year") `with` "2005"

      browser.fill("#hoursPerWeek") `with` "75"
      browser.fill("#jobTitle") `with` "Goblin"
      browser.fill("#payrollEmployeeNumber") `with` "445566"

      browser.submit("button[type='submit']")
      titleMustEqual("Employer contact details - Employment")
    }
  }
}