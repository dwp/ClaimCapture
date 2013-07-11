package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.{TestBrowser, WithBrowser}
import controllers.BrowserMatchers
import org.fluentlenium.core.Fluent

class G4TripIntegrationSpec extends Specification with Tags {
  "4 weeks trip" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")
      titleMustEqual("Trip - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new TripWithBrowser {
      trip(fourWeeks)
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.submit("button[value='next']")
      titleMustEqual("Trip - Time Spent Abroad")
      browser.$("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 four weeks trips in "trips table" upon providing 2 trips""" in new TripWithBrowser {
      trip(fourWeeks)
      trip(fourWeeks)
      titleMustEqual("Abroad for more than 4 weeks - Time Spent Abroad")
      browser.$("#trips table tbody tr").size shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new TripWithBrowser {
      pending
    }

    "add two trips and edit the second's start year" in new TripWithBrowser {
      pending
    }

    "allow cancellation" in new TripWithBrowser {
      pending
    }
  }

  "52 weeks trip" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/52Weeks")
      titleMustEqual("Trip - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new TripWithBrowser {
      trip(fiftyTwoWeeks)
      titleMustEqual("Abroad for more than 52 weeks - Time Spent Abroad")
    }
  }

  def fourWeeks(browser: TestBrowser) = browser.goTo("/timeSpentAbroad/trip/4Weeks")

  def fiftyTwoWeeks(browser: TestBrowser) = browser.goTo("/timeSpentAbroad/trip/52Weeks")

  class TripWithBrowser extends WithBrowser with BrowserMatchers {
    def trip(f: (TestBrowser) => Fluent) {
      f(browser)

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")
      browser.fill("#start_year") `with` "2000"

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.fill("#where") `with` "Scotland"
      browser.fill("#why") `with` "A sunny holiday"

      browser.submit("button[value='next']")
    }
  }
}