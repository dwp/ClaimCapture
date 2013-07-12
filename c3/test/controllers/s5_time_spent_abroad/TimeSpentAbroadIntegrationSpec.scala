package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import models.domain.Claiming

class TimeSpentAbroadIntegrationSpec extends Specification with Tags {
  "Time spent abroad" should {
    """present "completion" and proceed to "pay details".""" in new WithBrowser with BrowserMatchers with Claiming {
      browser.goTo("/timeSpentAbroad/normalResidenceAndCurrentLocation")
      titleMustEqual("Normal Residence and Current Location - Time Spent Abroad")

      browser.click("#liveInUK_answer_yes")
      browser.click("#inGBNow_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")

      browser.click("#anyTrips_no")
      browser.submit("button[value='next']")
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_no")
      browser.submit("button[value='next']")
      titleMustEqual("Other EEA State or Switzerland - Time Spent Abroad")

      browser.click("#benefitsFromOtherEEAStateOrSwitzerland_answer_no")
      browser.click("#workingForOtherEEAStateOrSwitzerland_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - Time Spent Abroad")

      browser.submit("button[value='next']")
      titleMustEqual("How We Pay You - Pay Details")
    }
  }
}