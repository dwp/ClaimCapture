package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class G3AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/abroadForMoreThan52Weeks")
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/abroadForMoreThan52Weeks")
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Trip - Time Spent Abroad")
    }

    """present "other EEA state or Switzerland" when no more 52 week trips are required""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/abroadForMoreThan52Weeks")
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_no")
      browser.submit("button[value='next']")
      titleMustEqual("Other EEA State or Switzerland - Time Spent Abroad")
    }

    """go back to "abroad for more than 4 weeks".""" in new WithBrowser with BrowserMatchers {
      pending
      /*browser.goTo("/timeSpentAbroad/abroadForMoreThan52Weeks")
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")*/
    }
  } section "integration"
}