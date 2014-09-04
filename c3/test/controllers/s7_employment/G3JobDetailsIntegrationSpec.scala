package controllers.s7_employment

import language.reflectiveCalls
import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G3JobDetailsIntegrationSpec extends Specification with Tags {
  "Your job" should {
    "present" in new WithBrowser with WithBrowserHelper {
      goTo("/employment/job-details/dummyJobID").title shouldEqual "Employer details - Employment History"
    }

    "accept only mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser.goTo(s"/employment/job-details/dummyJobID")
      browser.fill("#employerName") `with` "Toys r not Us"
      browser.fill("#phoneNumber") `with` "12345678"
      browser.fill("#address_lineOne") `with` "Street Test 1"
      browser.click("#jobStartDate_day option[value='1']")
      browser.click("#jobStartDate_month option[value='1']")
      browser.fill("#jobStartDate_year") `with` "2000"
      browser.click("#finishedThisJob_no")

      browser.submit("button[type='submit']")

      titleMustEqual("Your wages - Employment History")
    }

    "accept all data" in new WithBrowser with EmploymentFiller {
      jobDetails("dummyJobID")
    }

    """go back to "employment history".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmployedSinceClaimDate {
      beginClaim()
      back
      titleMustEqual("Employment Employment History")
    }

    "kick off a job, but not complete it and it should not be shown in the employment overview list " in new WithBrowser with WithBrowserHelper with EmployedSinceClaimDate with EmploymentFiller {
      beginClaim()

      jobDetails("dummyJobID1")
      goTo("/employment/been-employed")
      back
      $("#jobs table tbody tr").size() shouldEqual 0
    }

    "hours a week must be visible when clicked back" in new WithBrowser with WithBrowserHelper with BrowserMatchers with EmploymentFiller{
      jobDetails("dummyJobID")
      back
      findMustEqualSize("#hoursPerWeek", 1)
    }

  } section("integration", models.domain.Employed.id)

  trait EmploymentFiller extends BrowserMatchers {
    this: WithBrowser[_] =>

    def jobDetails(jobID: String) = {
      browser.goTo(s"/employment/job-details/$jobID")

      browser.fill("#employerName") `with` "Toys r not Us"
      browser.fill("#phoneNumber") `with` "12345678"
      browser.fill("#address_lineOne") `with` "Street Test 1"
      browser.click("#jobStartDate_day option[value='1']")
      browser.click("#jobStartDate_month option[value='1']")
      browser.fill("#jobStartDate_year") `with` "2000"
      browser.click("#finishedThisJob_yes")
      browser.click("#lastWorkDate_day option[value='1']")
      browser.click("#lastWorkDate_month option[value='1']")
      browser.fill("#lastWorkDate_year") `with` "2005"
      browser.click("#p45LeavingDate_day option[value='1']")
      browser.click("#p45LeavingDate_month option[value='1']")
      browser.fill("#p45LeavingDate_year") `with` "2005"
      browser.fill("#hoursPerWeek") `with` "75"

      browser.submit("button[type='submit']")

      titleMustEqual("Your wages - Employment History")
    }
  }
}