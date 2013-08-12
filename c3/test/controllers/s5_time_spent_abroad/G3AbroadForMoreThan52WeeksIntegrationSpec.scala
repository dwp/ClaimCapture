package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import play.api.i18n.Messages

class G3AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.click("#anyTrips_yes")
      browser.submit("button[value='next']")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.click("#anyTrips_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - Time Spent Abroad")
    }

    """go back to "abroad for more than 4 weeks".""" in new WithBrowser with BrowserMatchers {
      pending
      /*browser.goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")*/
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}