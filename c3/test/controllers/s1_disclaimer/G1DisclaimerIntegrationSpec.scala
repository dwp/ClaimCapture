package controllers.s1_disclaimer

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, Formulate, BrowserMatchers}
import utils.pageobjects.s_eligibility.GApprovePage
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_disclaimer.G1DisclaimerPage
import utils.pageobjects.s_claim_date.GClaimDatePage

class G1DisclaimerIntegrationSpec extends Specification with Tags {
  "Disclaimer" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G1DisclaimerPage(context)
      page goToThePage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val disclaimerPage = G1DisclaimerPage(context) goToThePage()
      val claimDatePage = disclaimerPage submitPage()

      claimDatePage must beAnInstanceOf[GClaimDatePage]
    }

    "navigate back to approve page" in new WithBrowser with PageObjects {
      val approvePage =  GApprovePage(context) goToThePage()
      val disclaimerPage = approvePage submitPage()

      disclaimerPage goBack() must beAnInstanceOf[GApprovePage]
    }

  } section("integration", models.domain.DisclaimerSection.id)
}