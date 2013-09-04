package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}

class G3AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      browser click "#anyTrips_yes"
      next
      titleMustEqual("Trips - Time Spent Abroad")
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      browser click "#anyTrips_no"
      next
      titleMustEqual("Completion - Time Spent Abroad")
    }

    """go back to "Details of time abroad with the person you care for".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-4-weeks"
      titleMustEqual("Details of time abroad with the person you care for - Time Spent Abroad")

      browser click "#anyTrips_no"
      next
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      back
      titleMustEqual("Details of time abroad with the person you care for - Time Spent Abroad")
    }

    """DE201 should not remember yes/no upon stating "52 weeks trips" and returning""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      browser.click("#anyTrips_yes")
      next

      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
        fill("#why") `with` "For Holidays"
      }

      trip()
      next
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      browser.findFirst("#anyTrips_yes").isSelected should beFalse
      browser.findFirst("#anyTrips_no").isSelected should beFalse
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}