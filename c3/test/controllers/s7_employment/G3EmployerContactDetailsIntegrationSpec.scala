package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G3EmployerContactDetailsIntegrationSpec extends Specification with Tags {
  "Employer's contact details" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employersContactDetails/dummyJobID")
      titleMustEqual("Employer contact details - Employment")
    }

    "accept only mandatory data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employersContactDetails/dummyJobID")
      browser.submit("button[type='submit']")
      titleMustEqual("Last wage - Employment")
    }

    "accept all data" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/employersContactDetails/dummyJobID")

      browser.fill("#address_lineOne") `with` "Employer's address line one"
      browser.fill("#postcode") `with` "EC1 1DA"
      browser.fill("#phoneNumber") `with` "0207 111 2222"

      browser.submit("button[type='submit']")
      titleMustEqual("Last wage - Employment")
    }

    """go back to "job details".""" in new WithBrowser with BrowserMatchers {
      skipped("Going back within a Job is not handled yet - going back is handled top level and we now have nested question groups")

      browser.goTo("/employment/jobDetails")
      browser.fill("#employerName") `with` "Toys r not Us"
      browser.click("#finishedThisJob_no")
      browser.submit("button[type='submit']")
      titleMustEqual("Employer contact details - Employment")

      browser.click("#backButton")
      titleMustEqual("Job Details - Employment")
    }
  } section "integration"
}