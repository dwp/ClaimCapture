package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G1YourDetailsPageContext, G9EmploymentPage}


class G9EmploymentIntegrationSpec extends Specification with Tags {
  sequential

  "Employment" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      browser.goTo("/about-you/employment")
      titleMustEqual("Employment - About you - the carer")
    }

    "be presented without claim date" in new WithBrowser with BrowserMatchers {
      browser.goTo("/about-you/employment")
      titleMustEqual("Does the person you care for get one of these benefits? - Can you get Carer's Allowance?")
    }

    "contain 7 completed forms" in new WithBrowser with BrowserMatchers {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)

      titleMustEqual("Employment - About you - the carer")
      findMustEqualSize("div[class=completed] ul li", 7)
    }

    "fill all fields" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      page goToThePage()
      page runClaimWith(claim, G9EmploymentPage.title)
    }

    "failed to fill the form" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")

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
      titleMustEqual("Partner/Spouse details - About your partner/spouse")
    }

  } section("integration", models.domain.AboutYou.id)
}