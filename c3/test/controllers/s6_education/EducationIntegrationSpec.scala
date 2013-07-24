package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import org.specs2.execute.PendingUntilFixed

class EducationIntegrationSpec extends Specification with Tags with PendingUntilFixed {
  "Education - Controller" should {
    """present "completion".""" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)

      titleMustEqual("Completion - Education")
    }

    "contain the completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 2
    }
    
    "back goes to 'Address Of School College Or University'" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      browser.click("#backButton")
      titleMustEqual("Address Of School College Or University - Education")
    }

    "redirect to the next section on clicking continue" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      browser.submit("button[type='submit']")

      titleMustNotEqual("Completion - Education")
    }

    "show the text 'Continue to Employment' on the submit button when next section is 'Employment'" in new WithBrowser {
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      
      browser.find("button[type='submit']").getText mustEqual "Continue to Employment"
    }
    
    "show the text 'Continue to Other Income' on the submit button when next section is 'Other Income'" in new WithBrowser {
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      
      browser.find("button[type='submit']").getText mustEqual "Continue to Other Income"
    }.pendingUntilFixed("Need a previous section to call hideSection on Employment")
  } section "unit"
}