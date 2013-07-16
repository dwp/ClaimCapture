package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.Formulate
import org.specs2.execute.PendingUntilFixed
import controllers.{BrowserMatchers, Formulate}


class G1YourCourseDetailsIntegrationSpec extends Specification with Tags with PendingUntilFixed {
  "Your Course Details Page" should {
    "be presented" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.title mustEqual "Your Course Details - Education"
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.fill("#startDate_year") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }
    
    "navigate to next page on valid submission with all fields filled in" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      Formulate.yourCourseDetails(browser)
      browser.title mustEqual "Address Of School College Or University - Education"
    }
        
    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.submit("button[type='submit']")
      browser.title mustEqual "Address Of School College Or University - Education"
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/yourCourseDetails")

      titleMustEqual("Your Course Details - Education")

      browser.click("#backButton")
      browser.title mustNotEqual "Your Course Details - Education"
    }
    
    "not be presented if section not visible" in new WithBrowser {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      browser.goTo("/education/yourCourseDetails")
      
      pending("need the first page of the Employment section to exist")
      
      browser.title mustEqual "Your employment history - About your Employment"
    }
  } section "integration"
}
