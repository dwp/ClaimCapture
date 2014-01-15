package controllers.s2_about_you

import language.reflectiveCalls
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{WithBrowserHelper, BrowserMatchers}
import implicits.Iteration._
import utils.pageobjects.s2_about_you.{G5AbroadForMoreThan52WeeksPage, G5AbroadForMoreThan52WeeksPageContext}
import utils.pageobjects.TestData

class G6TripIntegrationSpec extends Specification with Tags {
  "52 weeks trip" should {
    "be presented" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/trip/52-weeks")
      titleMustEqual("Trips - About you - carers")
    }

    "be submitted with all mandatory data" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        fill("#where") `with` "Scotland"

        click("#start_day option[value='1']")
        click("#start_month option[value='1']")
        fill("#start_year") `with` "2000"

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"

        fill("#why") `with` "For Holidays"
      }

      goTo("/about-you/trip/52-weeks")
      titleMustEqual("Trips - About you - carers")
      trip()
      next
      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
    }

    """give 2 errors when missing 2 mandatory fields of data - missing "start year" and "where".""" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      def trip() = {
        click("#start_day option[value='1']")
        click("#start_month option[value='1']")

        click("#end_day option[value='1']")
        click("#end_month option[value='1']")
        fill("#end_year") `with` "2000"
      }

      goTo("/about-you/trip/52-weeks")
      titleMustEqual("Trips - About you - carers")
      trip()
      next
      titleMustEqual("Trips - About you - carers")
      $("div[class=validation-summary] ol li") size() shouldEqual 3
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
        fill("#why") `with` "For Holidays"
      }

      2 x {
        goTo("/about-you/trip/52-weeks")
        titleMustEqual("Trips - About you - carers")
        trip()
        next
        titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
      }

      $("#trips table tbody tr") size() shouldEqual 2
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      pending
    }

    "add two trips and edit the second's start year" in new WithBrowser with G3AbroadForMoreThan52WeeksPageContext {
      val claim = new TestData
      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "Yes"
      // Trip
      claim.TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_1 = "10/04/2013"
      claim.TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_1 = "20/04/2013"
      claim.TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_1 = "Everywhere"
      claim.TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_1 = "Visit Family"
      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_2 = "Yes"
      claim.TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_2 = "10/05/2013"
      claim.TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_2 = "20/05/2013"
      claim.TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_2 = "Everywhere"
      claim.TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_2 = "Visit Family 2"
      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_3 = "No"

      page goToThePage()
      val anotherTrip3 = page runClaimWith (claim, G3AbroadForMoreThan52WeeksPage.title,3)
      val entry = anotherTrip3  goToPageFromIterationsTableAtIndex(1)  // index starts at 0
      entry fillDate("#start","09/05/2013")
      val newAnotherTrip3 = entry submitPage()
      assert(newAnotherTrip3.readTableCell(1,0).get contains "09/05/2013")
      //      def trip() = {
      //        click("#start_day option[value='1']")
      //        click("#start_month option[value='1']")
      //        fill("#start_year") `with` "2000"
      //
      //        click("#end_day option[value='1']")
      //        click("#end_month option[value='1']")
      //        fill("#end_year") `with` "2000"
      //
      //        fill("#where") `with` "Scotland"
      //        fill("#why") `with` "For Holidays"
      //      }
      //
      //      2 x {
      //        goTo("/about-you/trip/52-weeks")
      //        titleMustEqual("Trips - About you - carers")
      //        trip()
      //        next
      //        titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
      //      }
      //
      //      findFirst("input[value='Change']") click()
      //      titleMustEqual("Trips - About you - carers")
      //      $("#start_year") getValue() shouldEqual 2000.toString
      //
      //      fill("#start_year") `with` "1999"
      //      next
      //      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
      //
      //      $("tbody tr") size() shouldEqual 2
      //      $("tbody") findFirst "tr" findFirst "td" getText() must contain("1999")
    }

    "allow cancellation" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
      goTo("/about-you/abroad-for-more-than-52-weeks")
      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")

      goTo("/about-you/trip/52-weeks")
      titleMustEqual("Trips - About you - carers")

      back
      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
    }
  } section ("integration", models.domain.AboutYou.id)
}
