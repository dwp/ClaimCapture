package controllers.s2_about_you

import language.reflectiveCalls
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, WithBrowserHelper, BrowserMatchers}
import implicits.Iteration._
import utils.pageobjects.s2_about_you.{G6TripPage, G6TripPageContext, G5AbroadForMoreThan52WeeksPage, G5AbroadForMoreThan52WeeksPageContext}
import utils.pageobjects.TestData
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{JavascriptExecutor, By}
import java.util.concurrent.TimeUnit

class G6TripIntegrationSpec extends Specification with Tags {
  "52 weeks trip" should {
    "be presented" in new WithBrowser with WithBrowserHelper with G6TripPageContext {
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with G6TripPageContext {
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G6TripPage]
    }

    "be submitted with all mandatory data" in new WithBrowser with G6TripPageContext {
      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
    }
    
    "show 2 trips in trips table" in new WithBrowser with G6TripPageContext {
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage.iteration must beEqualTo(2)

      page2 goToThePage()
      page2 fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip2()

      val nextPage2 = page2 submitPage()

      nextPage2 must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage2.iteration must beEqualTo(3)
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with G6TripPageContext {
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage.iteration must beEqualTo(2)
      val v = nextPage.browser.pageSource()
      println(v)

      println("*********************************************************************")

      nextPage.browser.click("#iteration-1-delete")
      val w = nextPage.browser.pageSource()
      println(w)

      println("*********************************************************************")

      nextPage.browser.await.atMost(5, TimeUnit.SECONDS).until(".breaks-prompt").isPresent
      nextPage.browser.click("#selected-row-do-not-delete")
      val x = nextPage.browser.pageSource()
      println(x)

//      val element = nextPage.browser.webDriver.findElement(By.id("selected-row-delete"))
//      val executor = nextPage.browser.webDriver.asInstanceOf[JavascriptExecutor]
//      executor.executeScript("arguments[0].click();", element)
//
//      val y = nextPage.browser.click("#selected-row-delete").pageSource()

      nextPage goToThePage()

      println("************" + nextPage.browser.find("tbody tr"))
      nextPage.browser.find("tbody tr").size must beEqualTo(0)
    }
    //
//    "show zero trips after creating one and then deleting" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
//      pending
//    }
//
//    "add two trips and edit the second's start year" in new WithBrowser with G3AbroadForMoreThan52WeeksPageContext {
//      val claim = new TestData
//      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "Yes"
//      // Trip
//      claim.TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_1 = "10/04/2013"
//      claim.TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_1 = "20/04/2013"
//      claim.TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_1 = "Everywhere"
//      claim.TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_1 = "Visit Family"
//      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_2 = "Yes"
//      claim.TimeSpentAbroadDateYouLeftGBTripForMoreThan52Weeks_2 = "10/05/2013"
//      claim.TimeSpentAbroadDateYouReturnedToGBTripForMoreThan52Weeks_2 = "20/05/2013"
//      claim.TimeSpentAbroadWhereDidYouGoForMoreThan52Weeks_2 = "Everywhere"
//      claim.TimeSpentAbroadWhyDidYouGoForMoreThan52Weeks_2 = "Visit Family 2"
//      claim.TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime_3 = "No"
//
//      page goToThePage()
//      val anotherTrip3 = page runClaimWith (claim, G3AbroadForMoreThan52WeeksPage.title,3)
//      val entry = anotherTrip3  goToPageFromIterationsTableAtIndex(1)  // index starts at 0
//      entry fillDate("#start","09/05/2013")
//      val newAnotherTrip3 = entry submitPage()
//      assert(newAnotherTrip3.readTableCell(1,0).get contains "09/05/2013")
//      //      def trip() = {
//      //        click("#start_day option[value='1']")
//      //        click("#start_month option[value='1']")
//      //        fill("#start_year") `with` "2000"
//      //
//      //        click("#end_day option[value='1']")
//      //        click("#end_month option[value='1']")
//      //        fill("#end_year") `with` "2000"
//      //
//      //        fill("#where") `with` "Scotland"
//      //        fill("#why") `with` "For Holidays"
//      //      }
//      //
//      //      2 x {
//      //        goTo("/about-you/trip/52-weeks")
//      //        titleMustEqual("Trips - About you - carers")
//      //        trip()
//      //        next
//      //        titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
//      //      }
//      //
//      //      findFirst("input[value='Change']") click()
//      //      titleMustEqual("Trips - About you - carers")
//      //      $("#start_year") getValue() shouldEqual 2000.toString
//      //
//      //      fill("#start_year") `with` "1999"
//      //      next
//      //      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
//      //
//      //      $("tbody tr") size() shouldEqual 2
//      //      $("tbody") findFirst "tr" findFirst "td" getText() must contain("1999")
//    }
//
//    "allow cancellation" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
//      goTo("/about-you/abroad-for-more-than-52-weeks")
//      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
//
//      goTo("/about-you/trip/52-weeks")
//      titleMustEqual("Trips - About you - carers")
//
//      back
//      titleMustEqual("Details of time abroad for more than 52 weeks - About you - carers")
//    }
  } section ("integration", models.domain.AboutYou.id)
}
