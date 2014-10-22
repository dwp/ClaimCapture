package controllers.s2_about_you

import language.reflectiveCalls
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, WithBrowserHelper}
import utils.pageobjects.s2_about_you.{G6TripPage, G5AbroadForMoreThan52WeeksPage}
import play.api.i18n.Messages
import utils.pageobjects.{PageObjects, PageObjectsContext}

class G6TripIntegrationSpec extends Specification with Tags {
  "52 weeks trip" should {
    "be presented" in new WithBrowser with WithBrowserHelper with PageObjects{
			val page = G6TripPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  G6TripPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G6TripPage]
    }

    "be submitted with all mandatory data" in new WithBrowser with PageObjects {
        val page = G6TripPage(context)

        val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage()

        nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]

    }
    
    "show 2 trips in trips table" in new WithBrowser with PageObjects{
			val page =  G6TripPage(context)

      page goToThePage()
      page fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage.iteration must beEqualTo(2)

      val page2 = G6TripPage(PageObjectsContext(browser), iteration = 2)
      page2 goToThePage()
      page2 fillPageWith ClaimScenarioFactory.abroadForMoreThan52WeeksTrip2()

      val nextPage2 = page2 submitPage()

      nextPage2 must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage2.iteration must beEqualTo(2)
    }

    "show zero trips after creating one and then deleting" in new WithBrowser with PageObjects{
			val page =  G6TripPage(context)
      pending
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
    }




    "add trip and edit it" in new WithBrowser with PageObjects{
			val page =  G6TripPage(context)

      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      nextPage.iteration must beEqualTo(2)

      nextPage.ctx.browser.click("#iteration-1-change")
      nextPage.ctx.browser.pageSource() must contain(Messages("where"))
    }

    "go to 'abroad for more than 52 weeks' page then 'trips' page and then click back" in new WithBrowser with PageObjects{
			val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksConfirmationYes()
      page goToThePage()
      page fillPageWith claim

      val tripPage = page submitPage(waitForPage = true)
      tripPage must beAnInstanceOf[G6TripPage]
      tripPage goBack() must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
    }
  } section ("integration", models.domain.AboutYou.id)
}
