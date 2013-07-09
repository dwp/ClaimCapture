package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.WithBrowserAndMatchers

import utils.pageobjects.BenefitsPage
//
//import play.api.Logger

class G1BenefitsIntegrationSpec extends Specification with Tags {

  "Benefits" should {
    "be presented" in new WithBrowserAndMatchers {
      browser.goTo("/")
      titleMustEqual("Benefits - Carer's Allowance")
      browser.find("div[class=carers-allowance]").getText must contain("Q1")
    }


    /** the context */
    trait BenefitsPageContext extends Scope {
      val testBrowser = (new play.api.test.WithBrowser {}) browser
      val page = new BenefitsPage(testBrowser)
      page.goToPage
    }

    "allow changing answer via given link" in new WithBrowserAndMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      browser.click("div[class=completed] a")
      titleMustEqual("Benefits - Carer's Allowance")
      browser.find("#q3-yes").getAttribute("value") mustEqual "true"
    }

  "Does the person being cared for get one of required benefits" should {
    "acknowledge yes" in new WithBrowserAndMatchers {
      browser.goTo("/")
      browser.click("#q3-yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")
      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
      browser.find("div[class=completed] ul li").get(0).getText must contain("Yes")
    }

    "acknowledge no" in new WithBrowserAndMatchers {
      browser.goTo("/")
      browser.click("#q3-no")
      browser.submit("button[type='submit']")
      titleMustEqual("Hours - Carer's Allowance")
      browser.find("div[class=completed] ul li").get(0).getText must contain("Q1")
      browser.find("div[class=completed] ul li").get(0).getText must contain("No")
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