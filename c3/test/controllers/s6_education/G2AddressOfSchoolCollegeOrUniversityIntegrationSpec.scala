package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}
import org.specs2.execute.PendingUntilFixed

class G2AddressOfSchoolCollegeOrUniversityIntegrationSpec extends Specification with Tags with PendingUntilFixed {
  "Address of school, college or university" should {
    "be presented" in new WithBrowser {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.title mustEqual "Address Of School College Or University - Education"
    }

    "not be presented if section not visible" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.title mustNotEqual "Address Of School College Or University - Education"
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

    "navigate back to Your Course Details" in new WithBrowser {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.click("#backButton")
      browser.title mustEqual "Your Course Details - Education"
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser {
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      browser.title mustEqual "Completion - Education"
    }
    
    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser {
      browser.goTo("/education/addressOfSchoolCollegeOrUniversity")
      browser.submit("button[type='submit']")
      browser.title mustEqual "Completion - Education"
    }
  } section "integration"
}