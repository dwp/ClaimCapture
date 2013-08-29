package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G2AddressOfSchoolCollegeOrUniversityIntegrationSpec extends Specification with Tags {
  "Address of school, college or university" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/address-of-school-college-or-university")
      titleMustEqual("School, college or university's contact details - About your education")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/address-of-school-college-or-university")
      browser.fill("#postcode") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.yourCourseDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigate back to Your course details" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      titleMustEqual("School, college or university's contact details - About your education")
      browser.click("#backButton")
      titleMustEqual("Your course details - About your education")
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with BrowserMatchers {
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - About your education")
    }
    
    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/address-of-school-college-or-university")
      browser.submit("button[type='submit']")
      titleMustEqual("Completion - About your education")
    }
  } section("integration", models.domain.Education.id)
}