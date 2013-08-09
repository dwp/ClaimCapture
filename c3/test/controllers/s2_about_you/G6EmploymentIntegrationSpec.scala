package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G1YourDetailsPageContext, G7PropertyAndRentPage}

class G6EmploymentIntegrationSpec extends Specification with Tags {
  sequential

  "Employment" should {
    "be presented" in new WithBrowser {
      Formulate.claimDate(browser)
      browser.goTo("/aboutyou/employment")
      browser.title mustEqual "Employment - About You"
    }

    "be presented without claim date" in new WithBrowser {
      browser.goTo("/aboutyou/employment")
      browser.title mustEqual "Benefits - Can you get Carer's Allowance?"
    }

    "contain 4 completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)

      titleMustEqual("Employment - About You")
      browser.find("div[class=completed] ul li").size() mustEqual 4
    }

    "fill all fields" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      page goToThePage()
      page runClaimWith(claim, G7PropertyAndRentPage.title, waitForPage = true, waitDuration = 600)
    }

    "failed to fill the form" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More About You - About You")

      browser.goTo("/aboutyou/employment")
      titleMustEqual("Employment - About You")

      browser.submit("button[type='submit']")
      titleMustEqual("Employment - About You")

      browser.find("p[class=error]").size() mustEqual 2

      browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Employment - About You")

      browser.find("p[class=error]").size() mustEqual 1

      browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Property and Rent - About You")
    }
  } section("integration", models.domain.AboutYou.id)
}