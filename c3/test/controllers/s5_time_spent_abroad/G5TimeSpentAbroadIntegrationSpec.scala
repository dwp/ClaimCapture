package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.Formulate
import play.api.i18n.Messages

class G5TimeSpentAbroadIntegrationSpec extends Specification with Tags {
  "Time spent abroad" should {
    """present "completion" and proceed to 'Education'.""" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")

      Formulate.abroadForMoreThan4Weeks(browser)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      Formulate.abroadForMoreThan52Weeks(browser)
      titleMustEqual("Completion - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("Your course details - About your education")
    }

    "show the text 'Continue to Education' on the submit button when next section is 'Education'" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")

      Formulate.abroadForMoreThan4Weeks(browser)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      Formulate.abroadForMoreThan52Weeks(browser)
      titleMustEqual("Completion - Time Spent Abroad")

      browser.find("button[type='submit']").getText shouldEqual "Continue to Education"
    }

    "show the text 'Continue to Employment' on the submit button when next section is 'Employment'" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      titleMustEqual("Does the person you look after get one of these benefits? - Can you get Carer's Allowance?")

      Formulate.yourCourseDetails(browser)
      titleMustEqual("School, college or university's contact details - About your education")

      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - About your education")

      browser.find("button[type='submit']").getText shouldEqual "Continue to Employment"
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}