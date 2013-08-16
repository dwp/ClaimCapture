package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G1YourDetailsPageContext, G8AboutYouCompletedPage}
import play.api.i18n.Messages

class G6EmploymentIntegrationSpec extends Specification with Tags {
  sequential

  "Employment" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      browser.goTo("/about-you/employment")
      titleMustEqual("Employment - About you - the carer")
    }

    "be presented without claim date" in new WithBrowser with BrowserMatchers {
      browser.goTo("/about-you/employment")
      titleMustEqual("Does the person you look after get one of these benefits? - Can you get Carer's Allowance?")
    }

    "contain 4 completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.moreAboutYou(browser)

      titleMustEqual("Employment - About you - the carer")
      findMustEqualSize("div[class=completed] ul li", 4)
    }

    "fill all fields" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      page goToThePage()
      page runClaimWith(claim, G8AboutYouCompletedPage.title, waitForPage = true, waitDuration = 600)
    }

    "failed to fill the form" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("More about you - About you - the carer")

      browser.goTo("/about-you/employment")
      titleMustEqual("Employment - About you - the carer")

      browser.submit("button[type='submit']")
      titleMustEqual("Employment - About you - the carer")

      findMustEqualSize("p[class=error]", 2)

      browser.click("#beenEmployedSince6MonthsBeforeClaim_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Employment - About you - the carer")

      findMustEqualSize("p[class=error]", 1)

      browser.click("#beenSelfEmployedSince1WeekBeforeClaim_yes")
      browser.submit("button[type='submit']")
      titleMustEqual("Completion - About you - the carer")
    }
  } section("integration", models.domain.AboutYou.id)
}