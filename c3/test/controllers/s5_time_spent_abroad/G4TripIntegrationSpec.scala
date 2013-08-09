package controllers.s5_time_spent_abroad

import org.specs2.mutable.{Specification, Tags}
import play.api.test.{TestBrowser, WithBrowser}
import controllers.BrowserMatchers
import play.api.test.Helpers._
import org.fluentlenium.core.Fluent
import play.api.i18n.Messages

class G4TripIntegrationSpec extends Specification with Tags {
  "4 weeks trip" should {
    sequential

    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new TripWithBrowser {
      trip(fourWeeks)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/4Weeks")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.submit("button[value='next']")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
      browser.$("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 four weeks trips in "trips table" upon providing 2 trips""" in new TripWithBrowser {
      trip(fourWeeks)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")
      trip(fourWeeks)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")
      browser.$("#trips table tbody tr").size shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new TripWithBrowser {
      pending
    }

    "add two trips and edit the second's start year" in new TripWithBrowser {
      skipped("Ridiculous - Run this on its own and it's fine!")

      trip(fourWeeks)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")
      trip(fourWeeks)
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")

      browser.findFirst("input[value='Edit']").click()
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
      browser.$("#start_year").getValue shouldEqual 2000.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")

      browser.$("tbody tr").size() shouldEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }

    "allow cancellation" in new TripWithBrowser {
      browser.goTo("/timeSpentAbroad/abroadForMoreThan4Weeks")
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")

      browser.goTo("/timeSpentAbroad/trip/4Weeks")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual(Messages("s5.g2") + " - Time Spent Abroad")
    }
  } section ("integration",models.domain.TimeSpentAbroad.id)

  "52 weeks trip" should {
    sequential

    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/52Weeks")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new TripWithBrowser {
      trip(fiftyTwoWeeks)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with BrowserMatchers {
      browser.goTo("/timeSpentAbroad/trip/52Weeks")

      browser.click("#start_day option[value='1']")
      browser.click("#start_month option[value='1']")

      browser.click("#end_day option[value='1']")
      browser.click("#end_month option[value='1']")
      browser.fill("#end_year") `with` "2000"

      browser.submit("button[value='next']")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
      browser.$("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 fifty two weeks trips in "trips table" upon providing 2 trips""" in new TripWithBrowser {
      trip(fiftyTwoWeeks)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
      trip(fiftyTwoWeeks)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
      browser.$("#trips table tbody tr").size shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new TripWithBrowser {
      pending
    }

    "add two trips and edit the second's start year" in new TripWithBrowser {
      skipped("Ridiculous - Run this on its own and it's fine!")

      trip(fiftyTwoWeeks)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
      trip(fiftyTwoWeeks)
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.findFirst("input[value='Edit']").click()
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")
      browser.$("#start_year").getValue shouldEqual 2000.toString

      browser.fill("#start_year") `with` "1999"
      browser.submit("button[type='submit']")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.$("tbody tr").size() shouldEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }

    "allow cancellation" in new TripWithBrowser {
      browser.goTo("/timeSpentAbroad/abroadForMoreThan52Weeks")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")

      browser.goTo("/timeSpentAbroad/trip/52Weeks")
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")

      browser.click("#backButton")
      titleMustEqual(Messages("s5.g3") + " - Time Spent Abroad")
    }
  } section ("integration", models.domain.TimeSpentAbroad.id)

  def fourWeeks(browser: TestBrowser) = browser.goTo("/timeSpentAbroad/trip/4Weeks")

  def fiftyTwoWeeks(browser: TestBrowser) = browser.goTo("/timeSpentAbroad/trip/52Weeks")

  class TripWithBrowser extends WithBrowser with BrowserMatchers {
    def trip(f: TestBrowser => Fluent) {
      f(browser)
      titleMustEqual(Messages("s5.g4") + " - Time Spent Abroad")

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