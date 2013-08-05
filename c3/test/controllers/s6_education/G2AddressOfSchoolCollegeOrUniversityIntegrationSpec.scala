package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class G2AddressOfSchoolCollegeOrUniversityIntegrationSpec extends Specification with Tags {
  "Address of school, college or university" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      titleMustEqual("Address Of School College Or University - Education")
    }

    "not be presented if section not visible" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      titleMustNotEqual("Address Of School College Or University - Education")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.fill("#postcode") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }
    
    "contain the completed forms" in new WithBrowser {
      Formulate.yourCourseDetails(browser)
      browser.find("div[class=completed] ul li").size() mustEqual 1
    }

    "navigate back to Your Course Details" in new WithBrowser with BrowserMatchers {
      Formulate.yourCourseDetails(browser)
      titleMustEqual("Address Of School College Or University - Education")(Duration(10, TimeUnit.MINUTES))
      browser.click("#backButton")
      titleMustEqual("Your Course Details - Education")(Duration(10, TimeUnit.MINUTES))
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with BrowserMatchers {
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - Education")
    }
    
    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.submit("button[type='submit']")
      titleMustEqual("Completion - Education")
    }
  } section "integration"
}