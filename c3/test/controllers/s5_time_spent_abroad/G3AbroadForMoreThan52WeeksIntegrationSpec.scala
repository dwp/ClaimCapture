package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G3AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser click "#anyTrips_yes"
      next
      titleMustEqual("Trips - Time Spent Abroad")
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser click "#anyTrips_no"
      next
      titleMustEqual("Completion - Time Spent Abroad")
    }

    """go back to "abroad for more than 4 weeks".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-4-weeks"
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser click "#anyTrips_no"
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }

    """not record the "yes/no" answer upon starting to add a new 52 weeks trip but "cancel".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_yes")
      next
      titleMustEqual("Trips - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      browser.findFirst("#anyTrips_yes").isSelected should beFalse
      browser.findFirst("#anyTrips_no").isSelected should beFalse
    }

    """allow a new 52 weeks trip to be added but not record the "yes/no" answer""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_yes")
      next
      titleMustEqual("Trips - Time Spent Abroad")

      trip()
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser.findFirst("#anyTrips_yes").isSelected should beFalse
      browser.findFirst("#anyTrips_no").isSelected should beFalse
    }

    """remember "no more 52 weeks trips" upon stating "52 weeks trips" and returning""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser.click("#anyTrips_no")
      next
      titleMustEqual("Completion - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      browser.findFirst("#anyTrips_yes").isSelected should beFalse
      browser.findFirst("#anyTrips_no").isSelected should beTrue
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}