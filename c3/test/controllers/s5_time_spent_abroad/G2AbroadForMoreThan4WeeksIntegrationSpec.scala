package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}
import play.api.i18n.Messages

class G2AbroadForMoreThan4WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 4 weeks" should {
    "present" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }

    "provide for trip entry" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser.click("#anyTrips_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Trips - Time Spent Abroad")
    }

    """present "52 weeks trips" when no more 4 week trips are required""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser.click("#anyTrips_no")
      browser.submit("button[value='next']")
      titleMustEqual("When you went abroad for more than 52 - Time Spent Abroad")
    }

    """go back to "normal residence and current location".""" in new WithBrowser with BrowserMatchers {
      Formulate.normalResidenceAndCurrentLocation(browser)
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual("Your normal residence and current location - Time Spent Abroad")
    }

    /*"""allow a new 4 weeks trip to be added but not record the "yes/no" answer""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser.click("#answer_yes")
      browser.submit("button[value='next']")
      titleMustEqual("Trips - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
      browser.findFirst("#answer_yes").isSelected should beFalse
    }*/

    /*"""remember "no more breaks" upon stating "no more breaks" and returning to "breaks in care".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/care-you-provide/breaks-in-care")
      titleMustEqual("Breaks in care - About the care you provide")

      browser.click("#answer_no")
      browser.submit("button[value='next']")
      titleMustEqual("Completion - About the care you provide")

      browser.click("#backButton")
      titleMustEqual("Breaks in care - About the care you provide")
      browser.findFirst("#answer_no").isSelected should beTrue
    }*/
  } section("integration", models.domain.TimeSpentAbroad.id)
}