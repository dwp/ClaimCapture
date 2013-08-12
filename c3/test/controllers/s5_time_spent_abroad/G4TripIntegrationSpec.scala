package controllers.s5_time_spent_abroad

import language.reflectiveCalls
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Navigation, BrowserMatchers}
import implicits.Iteration._

class G4TripIntegrationSpec extends Specification with Tags {
  "4 weeks trip" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser goTo "/time-spent-abroad/trip/4-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      browser goTo "/time-spent-abroad/trip/4-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"
      }

      browser goTo "/time-spent-abroad/trip/4-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("Trips - Time Spent Abroad")
      browser.$("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 four weeks trips in "trips table" upon providing 2 trips""" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      2 times {
        browser goTo "/time-spent-abroad/trip/4-weeks"
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
      }

      browser.$("#trips table tbody tr").size shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with Navigation with BrowserMatchers {
      pending
    }

    "add two trips and edit the second's start year" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      2 times {
        browser goTo "/time-spent-abroad/trip/4-weeks"
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
      }

      browser findFirst "input[value='Change']" click()
      titleMustEqual("Trips - Time Spent Abroad")
      browser.$("#start_year").getValue shouldEqual 2000.toString

      browser fill "#start_year" `with` "1999"
      next
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser.$("tbody tr").size() shouldEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }

    "allow cancellation" in new WithBrowser with Navigation with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-4-weeks"
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      browser goTo "/time-spent-abroad/trip/4-weeks"
      titleMustEqual("Trips - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }
  } section ("integration", models.domain.TimeSpentAbroad.id)

  "52 weeks trip" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser goTo "/time-spent-abroad/trip/52-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      browser goTo "/time-spent-abroad/trip/52-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"
      }

      browser goTo "/time-spent-abroad/trip/52-weeks"
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("Trips - Time Spent Abroad")
      browser.$("div[class=validation-summary] ol li").size shouldEqual 2
    }

    """show 2 fifty two weeks trips in "trips table" upon providing 2 trips""" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      2 times {
        browser goTo "/time-spent-abroad/trip/52-weeks"
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      }

      browser.$("#trips table tbody tr").size shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with Navigation with BrowserMatchers {
      pending
    }

    "add two trips and edit the second's start year" in new WithBrowser with Navigation with BrowserMatchers {
      def trip() = {
        browser click "#start_day option[value='1']"
        browser click "#start_month option[value='1']"
        browser fill "#start_year" `with` "2000"

        browser click "#end_day option[value='1']"
        browser click "#end_month option[value='1']"
        browser fill "#end_year" `with` "2000"

        browser fill "#where" `with` "Scotland"
      }

      2 times {
        browser goTo "/time-spent-abroad/trip/52-weeks"
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      }

      browser findFirst "input[value='Change']" click()
      titleMustEqual("Trips - Time Spent Abroad")
      browser.$("#start_year").getValue shouldEqual 2000.toString

      browser fill "#start_year" `with` "1999"
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser.$("tbody tr").size() shouldEqual 2
      browser.$("tbody").findFirst("tr").findFirst("td").getText must contain("1999")
    }

    "allow cancellation" in new WithBrowser with Navigation with BrowserMatchers {
      browser goTo "/time-spent-abroad/abroad-for-more-than-52-weeks"
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      browser goTo "/time-spent-abroad/trip/52-weeks"
      titleMustEqual("Trips - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
    }
  } section ("integration", models.domain.TimeSpentAbroad.id)
}