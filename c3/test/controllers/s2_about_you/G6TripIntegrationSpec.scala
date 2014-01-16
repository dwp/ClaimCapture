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
import play.api.i18n.Messages

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

      val page2 = G6TripPage(browser = browser, iteration = 2)
      page2 goToThePage()
      page2 fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip2()

      val nextPage2 = page2 submitPage()

      nextPage2 must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage2.iteration must beEqualTo(3)
    }

//    "show zero trips after creating one and then deleting" in new WithBrowser with G6TripPageContext {
//      page goToThePage()
//      page fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
//
//      val nextPage = page submitPage()
//
//      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
//      nextPage.iteration must beEqualTo(2)
//      val v = nextPage.browser.pageSource()
//      println(v)
//
//      println("*********************************************************************")
//
//      nextPage.browser.click("#iteration-1-delete")
//      val w = nextPage.browser.pageSource()
//      println(w)
//
//      println("*********************************************************************")
//
//      nextPage.browser.await.atMost(5, TimeUnit.SECONDS).until(".breaks-prompt").isPresent
//      nextPage.browser.click("#selected-row-do-not-delete")
//      val x = nextPage.browser.pageSource()
//      println(x)
//
////      val element = nextPage.browser.webDriver.findElement(By.id("selected-row-delete"))
////      val executor = nextPage.browser.webDriver.asInstanceOf[JavascriptExecutor]
////      executor.executeScript("arguments[0].click();", element)
////
////      val y = nextPage.browser.click("#selected-row-delete").pageSource()
//
//      nextPage goToThePage()
//
//      println("************" + nextPage.browser.find("tbody tr"))
//      nextPage.browser.find("tbody tr").size must beEqualTo(0)
//    }


    //
//    "show zero trips after creating one and then deleting" in new WithBrowser with WithBrowserHelper with BrowserMatchers {
//      pending
//    }

    "add trip and edit it" in new WithBrowser with G6TripPageContext {
      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage.iteration must beEqualTo(2)

      nextPage.browser.click("#iteration-1-change")
      nextPage.browser.pageSource() must contain(Messages("where"))
    }

    "go to 'abroad for more than 52 weeks' page then 'trips' page and then click back" in new WithBrowser with G5AbroadForMoreThan52WeeksPageContext {
      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksConfirmation()
      page goToThePage()
      page fillPageWith claim

      val tripPage = page submitPage(waitForPage = true)
      tripPage must beAnInstanceOf[G6TripPage]
      tripPage goBack() must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
    }
  } section ("integration", models.domain.AboutYou.id)
}
