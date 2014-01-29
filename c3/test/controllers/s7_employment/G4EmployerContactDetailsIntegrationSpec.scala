package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G4EmployerContactDetailsIntegrationSpec extends Specification with Tags {
  "Employer's contact details" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employers-contact-details/dummyJobID")
      titleMustEqual("Employer's contact details - Employment History")
    }

    "accept only mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employers-contact-details/dummyJobID")
      browser.submit("button[type='submit']")
      titleMustEqual("Employer's contact details - Employment History")
    }

    "accept all data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employers-contact-details/dummyJobID")

      browser.fill("#address_lineOne") `with` "Employers address line one"
      browser.fill("#postcode") `with` "EC1 1DA"
      browser.fill("#phoneNumber") `with` "0207 111 2222"

      browser.submit("button[type='submit']")
      titleMustEqual("Your last wage - Employment History")
    }

    """go back to "job details".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/job-details/dummyJobID")
      fill("#employerName") `with` "Toys r not Us"
      browser.click("#jobStartDate_day option[value='1']")
      browser.click("#jobStartDate_month option[value='1']")
      browser.fill("#jobStartDate_year") `with` "2000"
      click("#finishedThisJob_no")
      next
      titleMustEqual("Employer's contact details - Employment History")

      back
      titleMustEqual("Your job - Employment History")
    }

    "get first completed question group for a job" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/employment/job-details/dummyJobID")
      fill("#employerName") `with` "Toys r not Us"
      browser.click("#jobStartDate_day option[value='1']")
      browser.click("#jobStartDate_month option[value='1']")
      browser.fill("#jobStartDate_year") `with` "2000"
      click("#finishedThisJob_no")
      next
      titleMustEqual("Employer's contact details - Employment History")
      findAll("div[class=completed] ul li").size shouldEqual 1
    }
  } section("integration", models.domain.Employed.id)
}