package controllers.s7_employment

import language.reflectiveCalls
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}
import implicits.Iteration._

class G2JobDetailsIntegrationSpec extends Specification with Tags {
  "Your job" should {
    "present" in new WithBrowser with WithBrowserHelper {
      goTo("/employment/job-details").title shouldEqual "Your job - Employment History"
    }

    "show 2 errors upon submitting no mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/job-details")
      next
      titleMustEqual("Your job - Employment History")
      findMustEqualSize("div[class=validation-summary] ol li", 2)
    }

    "accept only mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/job-details")
      fill("#employerName") `with` "Toys r not Us"
      click("#finishedThisJob_no")

      next.title shouldEqual "Employer's contact details - Employment History"
    }

    "accept all data" in new WithBrowser with EmploymentFiller {
      jobDetails()
    }

    """go back to "been employed?".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim()

      goTo("/employment/been-employed").click("#beenEmployed_yes")
      next
      goTo("/employment/job-details")
      back
      titleMustEqual("Your employment history - Employment History")
    }

    "begin twice, kicking off 2 jobs and choose to start editing the first job" in new WithBrowser with WithBrowserHelper with EmployedSinceClaimDate with EmploymentFiller {
      beginClaim()

      2 x { jobDetails() }

      goTo("/employment/been-employed")
      $("#jobs table tbody tr").size() shouldEqual 2

      findFirst("input[value='Change']").click()
      titleMustEqual("Your job - Employment History")
    }
  } section("integration", models.domain.Employed.id)

  trait EmploymentFiller extends BrowserMatchers {
    this: WithBrowser[_] =>

    def jobDetails() = {
      browser.goTo("/employment/job-details")

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
      titleMustEqual("Employer's contact details - Employment History")
    }
  }
}