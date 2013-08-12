package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, Formulate}

class G1YourCourseDetailsIntegrationSpec extends Specification with Tags {
  "Your Course Details Page" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/your-course-details")
      titleMustEqual("Your Course Details - About your education")
    }

    "not be presented if section not visible" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      browser.goTo("/education/your-course-details")

      titleMustNotEqual("Your Course Details - About your education")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/your-course-details")
      browser.fill("#startDate_year") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/your-course-details")
      Formulate.yourCourseDetails(browser)

      titleMustEqual("School, college or university's contact details - About your education")
    }

    "navigate to next page on valid submission with only mandatory fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/your-course-details")
      browser.submit("button[type='submit']")

      titleMustEqual("School, college or university's contact details - About your education")
    }

    "navigate back" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/your-course-details")
      browser.click("#backButton")
      titleMustNotEqual("Your Course Details - About your education")
    }

  } section("integration", models.domain.Education.id)
}