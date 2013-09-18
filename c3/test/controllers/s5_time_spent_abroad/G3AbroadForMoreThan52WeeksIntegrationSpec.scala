package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, WithBrowserHelper}

class G3AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      click("#anyTrips_yes")
      next
      titleMustEqual("Trips - Time Spent Abroad")
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      click("#anyTrips_no")
      next
      titleMustEqual("Completion - Time Spent Abroad")
    }

    """go back to "Details of time abroad with the person you care for".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("Details of time abroad with the person you care for - Time Spent Abroad")

      click("#anyTrips_no")
      next
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      back
      titleMustEqual("Details of time abroad with the person you care for - Time Spent Abroad")
    }

    """remember "no more 52 weeks trips" upon stating "52 weeks trips" and returning""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")

      click("#anyTrips_no")
      next
      titleMustEqual("Completion - Time Spent Abroad")

      back
      titleMustEqual("Details of time abroad for more than 52 weeks - Time Spent Abroad")
      findFirst("#anyTrips_yes").isSelected should beFalse
      findFirst("#anyTrips_no").isSelected should beTrue
    }
  } section("integration", models.domain.TimeSpentAbroad.id)
}