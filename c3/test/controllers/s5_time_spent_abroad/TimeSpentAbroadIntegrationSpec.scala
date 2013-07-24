package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.Formulate

class TimeSpentAbroadIntegrationSpec extends Specification with Tags {
  "Time spent abroad" should {
    """present "completion" and proceed to 'Education'.""" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      Formulate.abroadForMoreThan4Weeks(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      
      titleMustEqual("Completion - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("Your Course Details - Education")
    }
    
    "navigate to" in {
      "show the text 'Continue to Education' on the submit button when next section is 'Education'" in new WithBrowser with BrowserMatchers {
        Formulate.normalResidenceAndCurrentLocation(browser)
        Formulate.abroadForMoreThan4Weeks(browser)
        Formulate.abroadForMoreThan52Weeks(browser)
        Formulate.otherEEAStateOrSwitzerland(browser)
      
        browser.find("button[type='submit']").getText mustEqual "Continue to Education"
      }
      /*
      "show the text 'Continue to Other Income' on the submit button when next section is 'Other Income'" in new WithBrowser {
        Formulate.moreAboutYouNotBeenInEducationSinceClaimDate(browser)
        Formulate.yourCourseDetails(browser)
        Formulate.addressOfSchoolCollegeOrUniversity(browser)
        
        browser.find("button[type='submit']").getText mustEqual "Continue to Other Income"
      }.pendingUntilFixed("Need a previous section to call hideSection on Employment")*/
    }
  }
}