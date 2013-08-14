package controllers.s5_time_spent_abroad

import language.reflectiveCalls
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}
import implicits.Iteration._

class G4TripIntegrationSpec extends Specification with Tags {
  "4 weeks trip" should {
    "be presented" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/trip/4-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      goTo("/time-spent-abroad/trip/4-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"
      }

      goTo("/time-spent-abroad/trip/4-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("Trips - Time Spent Abroad")
      $("div[class=validation-summary] ol li") size() shouldEqual 2
    }

    """show 2 four weeks trips in "trips table" upon providing 2 trips""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      2 times {
        goTo("/time-spent-abroad/trip/4-weeks")
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
      }

      $("#trips table tbody tr") size() shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      pending
    }

    "add two trips and edit the second's start year" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      2 times {
        goTo("/time-spent-abroad/trip/4-weeks")
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
      }

      findFirst("input[value='Change']") click()
      titleMustEqual("Trips - Time Spent Abroad")
      $("#start_year") getValue() shouldEqual 2000.toString

      fill("#start_year") `with` "1999"
      next
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      $("tbody tr") size() shouldEqual 2
      $("tbody") findFirst "tr" findFirst "td" getText() must contain("1999")
    }

    "allow cancellation" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-4-weeks")
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")

      goTo("/time-spent-abroad/trip/4-weeks")
      titleMustEqual("Trips - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 4 weeks - Time Spent Abroad")
    }
  } section ("integration", models.domain.TimeSpentAbroad.id)

  "52 weeks trip" should {
    "be presented" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/trip/52-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
    }

    "be submitted with all mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      goTo("/time-spent-abroad/trip/52-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"
      }

      goTo("/time-spent-abroad/trip/52-weeks")
      titleMustEqual("Trips - Time Spent Abroad")
      trip()
      next
      titleMustEqual("Trips - Time Spent Abroad")
      $("div[class=validation-summary] ol li") size() shouldEqual 2
    }

    """show 2 fifty two weeks trips in "trips table" upon providing 2 trips""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      2 times {
        goTo("/time-spent-abroad/trip/52-weeks")
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      }

      $("#trips table tbody tr") size() shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      pending
    }

    "add two trips and edit the second's start year" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#where") `with` "Scotland"
      }

      2 times {
        goTo("/time-spent-abroad/trip/52-weeks")
        titleMustEqual("Trips - Time Spent Abroad")
        trip()
        next
        titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
      }

      findFirst("input[value='Change']") click()
      titleMustEqual("Trips - Time Spent Abroad")
      $("#start_year") getValue() shouldEqual 2000.toString

      fill("#start_year") `with` "1999"
      next
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      $("tbody tr") size() shouldEqual 2
      $("tbody") findFirst "tr" findFirst "td" getText() must contain("1999")
    }

    "allow cancellation" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/time-spent-abroad/abroad-for-more-than-52-weeks")
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")

      goTo("/time-spent-abroad/trip/52-weeks")
      titleMustEqual("Trips - Time Spent Abroad")

      back
      titleMustEqual("When you went abroad for more than 52 weeks - Time Spent Abroad")
    }
  } section ("integration", models.domain.TimeSpentAbroad.id)
}