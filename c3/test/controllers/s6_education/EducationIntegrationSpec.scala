package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

class EducationIntegrationSpec extends Specification with Tags {
  "Education" should {
    """present "completion".""" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)

      titleMustEqual("Completion - Education")(Duration(10, TimeUnit.MINUTES))
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - Education")

      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
    
    "back goes to 'Address Of School College Or University'" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - Education")

      browser.click("#backButton")
      titleMustEqual("Address Of School College Or University - Education")
    }

    "show the text 'Continue to Other Money' on the submit button when next section is 'Other Money'" in new WithBrowser with BrowserMatchers {
      pending("Skipped till show/hide employment logic is implemented")
    }

    "show the text 'Continue to Employment' on the submit button when next section is 'Employment'" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - Education")(Duration(10, TimeUnit.MINUTES))

      browser.find("button[type='submit']").getText mustEqual "Continue to Employment"
      browser.submit("button[type='submit']")
      titleMustEqual("Job Details - Employment")(Duration(10, TimeUnit.MINUTES))
    }
  } section "integration"
}