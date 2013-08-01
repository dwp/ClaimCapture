package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.Formulate

class G5TimeSpentAbroadIntegrationSpec extends Specification with Tags {
  "Time spent abroad" should {
    """present "completion" and proceed to 'Education'.""" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      Formulate.abroadForMoreThan4Weeks(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      
      titleMustEqual("Completion - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("Your Course Details - Education")
    }

    "show the text 'Continue to Education' on the submit button when next section is 'Education'" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      Formulate.abroadForMoreThan4Weeks(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      titleMustEqual("Completion - Time Spent Abroad")

      browser.find("button[type='submit']").getText shouldEqual "Continue to Education"
    }

    "show the text 'Continue to Employment' on the submit button when next section is 'Employment'" in new WithBrowser with BrowserMatchers {
      Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
      Formulate.yourCourseDetails(browser)
      Formulate.addressOfSchoolCollegeOrUniversity(browser)
      titleMustEqual("Completion - Education")

      browser.find("button[type='submit']").getText mustEqual "Continue to Employment"
    }
  } section "integration"
}