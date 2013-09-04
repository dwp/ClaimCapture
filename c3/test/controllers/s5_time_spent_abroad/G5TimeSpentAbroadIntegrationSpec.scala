package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.Formulate

class G5TimeSpentAbroadIntegrationSpec extends Specification with Tags {
  "Time spent abroad" should {
    """present "completion" and proceed to 'Education'.""" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")

      Formulate.abroadForMoreThan4Weeks(browser)
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      Formulate.abroadForMoreThan52Weeks(browser)
      titleMustEqual("Completion - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("Your course details - About your education")
    }

    "show the text 'Continue to education' on the submit button when next section is 'Education'" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")

      Formulate.abroadForMoreThan4Weeks(browser)
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      Formulate.abroadForMoreThan52Weeks(browser)
      titleMustEqual("Completion - Time Spent Abroad")

      browser.find("button[type='submit']").getText shouldEqual "Continue to education"
    }

    "show the text 'Continue to employment' on the submit button when next section is 'Employment'" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      titleMustEqual("Does the person you care for get one of these benefits? - Can you get Carer's Allowance?")

      Formulate.yourCourseDetails(browser)
      titleMustEqual("School, college or university's contact details - About your education")

      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - About your education")

      browser.find("button[type='submit']").getText shouldEqual "Continue to employment"
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}