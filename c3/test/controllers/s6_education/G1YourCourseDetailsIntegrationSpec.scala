package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G1YourCourseDetailsIntegrationSpec extends Specification with Tags {
  "Your Course Details Page" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/yourCourseDetails")
      titleMustEqual("Your Course Details - Education")
    }

    "not be presented if section not visible" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      browser.goTo("/education/yourCourseDetails")

      titleMustNotEqual("Your Course Details - Education")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/yourCourseDetails")
      browser.fill("#startDate_year") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/yourCourseDetails")
      Formulate.yourCourseDetails(browser)

      titleMustEqual("Address Of School College Or University - Education")
    }

    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/yourCourseDetails")
      browser.submit("button[type='submit']")

      titleMustEqual("Address Of School College Or University - Education")
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/yourCourseDetails")
      browser.click("#backButton")
      titleMustNotEqual("Your Course Details - Education")
    }

  } section "integration"
}
