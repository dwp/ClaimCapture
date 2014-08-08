package controllers.s6_education

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s2_about_you.G1YourDetailsPage
import utils.pageobjects.PageObjectsContext
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.s6_education.G1YourCourseDetailsPage
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage

class G1YourCourseDetailsIntegrationSpec extends Specification with Tags {
  "Your course details Page" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/education/your-course-details")
      titleMustEqual("Your course details - About your education")
    }

    "contain errors on invalid submission" in new WithBrowser {
      browser.goTo("/education/your-course-details")
      browser.click("#beenInEducationSinceClaimDate_yes")
      browser.fill("#startDate_year") `with` "INVALID"
      browser.submit("button[type='submit']")
      browser.find("div[class=validation-summary] ol li").size mustEqual 1
    }

    "show the text 'Continue to Other Money' on the submit button when next section is 'Other Money'" in new WithBrowser with BrowserMatchers {
      pending("Skipped till show/hide employment logic is implemented")
    }

    "navigate to next page on valid submission with all fields filled in" in new WithBrowser with BrowserMatchers {
      browser.goTo("/employment/been-employed")
      Formulate.claimDate(browser)
      Formulate.employment(browser)
      Formulate.yourCourseDetails(browser)

      titleMustEqual("Employment Employment History")
     }

    "navigate back" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/breaks-in-care")

      browser.goTo("/education/your-course-details")
      browser.click("#backButton")
      titleMustNotEqual("Your course details - About your education")
    }
  } section("integration", models.domain.Education.id)
}