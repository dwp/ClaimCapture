package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.WithBrowserAndMatchers
import play.api.test.WithBrowser
import org.specs2.specification.Scope
//import utils.pageobjects.{BenefitsPageContext, BenefitsPage}

import utils.pageobjects.BenefitsPage
//
//import play.api.Logger

class G1BenefitsIntegrationSpec extends Specification with Tags {

  "Benefits" should {


    /** the context */
    trait BenefitsPageContext extends Scope {
      val testBrowser = (new play.api.test.WithBrowser {}) browser
      val page = new BenefitsPage(testBrowser)
      page.goToPage
    }


    //    "be presented" in new WithBrowserAndMatchers {
    //      browser.goTo("/")
    //      titleMustEqual("Benefits - Carer's Allowance")
    //      browser.find("div[class=carers-allowance]").getText must contain("Q1")
    //    }

    "be presented" in new BenefitsPageContext {
      //  val page = new BenefitsPage(browser)

      page.isInQ1Page must beTrue
    }

    //    "allow changing answer" in new WithBrowser {
    //      browser.goTo("/")
    //      browser.click("#q3-yes")
    //      browser.submit("button[type='submit']")
    //
    //      browser.goTo("/allowance/benefits?changing=true")
    //      browser.find("#q3-yes").getAttribute("value") mustEqual "true"
    //    }

    "allow changing answer" in new BenefitsPageContext {
      //      val page = new BenefitsPage(browser)
      //      page.goToPage
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      testBrowser.goTo("/allowance/benefits?changing=true")
      page.doesPersonGetBenefit must beTrue
    }

    //    "allow changing answer via given link" in new BenefitsPage {
    //      page clickPersonGetsBenefits()
    //      val nextPage = page submitPage()
    //      nextPage clickChangeBenefitsDetails()
    //      page.isInBenefitsPage must beTrue
    //      page.doesPersonGetBenefit must beTrue
    //    }
    //  } section "integration"
    //
    //
    //
    //  "Does the person being cared for get one of required benefits" should {

    //    "acknowledge yes 1" in new WithBrowserAndMatchers {
    //      browser.goTo("/")
    //      browser.click("#q3-yes")
    //      browser.submit("button[type='submit']")
    //      titleMustEqual("Hours - Carer's Allowance")
    //      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
    //      browser.find("div[class=completed] ul li").get(0).getText must contain("Yes")
    //    }

    "acknowledge yes" in new BenefitsPageContext {
//            val page = new BenefitsPage(browser)
//            page.goToPage
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      //      Logger.error("title " + browser.title())

      nextPage.isInHoursPage
      nextPage.isQ1Yes must beTrue

      //      titleMustEqual("Hours - Carer's Allowance")
      //            browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
      //            browser.find("div[class=completed] ul li").get(0).getText must contain("Yes")
    }

    //    "acknowledge no" in new WithBrowserAndMatchers {
    //      browser.goTo("/")
    //      browser.click("#q3-no")
    //      browser.submit("button[type='submit']")
    //      titleMustEqual("Hours - Carer's Allowance")
    //      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
    //      browser.find("div[class=completed] ul li").get(0).getText must contain("No")
    //    }

    //    "acknowledge no" in new WithBrowser {
    //      val page = new BenefitsPage(browser)
    //      page.goToPage
    //      page clickPersonDoesNotGetBenefits()
    //      val nextPage = page submitPage()
    //      nextPage.isInHoursPage
    ////      nextPage.isQ1No must beTrue
    //            browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
    //            browser.find("div[class=completed] ul li").get(0).getText must contain("No")
    //    }

  } section "integration"
}