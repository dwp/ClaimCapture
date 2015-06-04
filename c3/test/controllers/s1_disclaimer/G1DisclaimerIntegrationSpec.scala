package controllers.s1_disclaimer

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, Formulate, BrowserMatchers}
import utils.pageobjects.s0_carers_allowance.G6ApprovePage
import utils.pageobjects.PageObjects
import utils.pageobjects.s1_disclaimer.G1DisclaimerPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G1DisclaimerIntegrationSpec extends Specification with Tags {
  "Disclaimer" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G1DisclaimerPage(context)
      page goToThePage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val disclaimerPage = G1DisclaimerPage(context) goToThePage()
      val claimDatePage = disclaimerPage submitPage()

      claimDatePage must beAnInstanceOf[G1ClaimDatePage]
    }

    "navigate back to approve page" in new WithBrowser with PageObjects {
      val approvePage =  G6ApprovePage(context) goToThePage()
      val disclaimerPage = approvePage submitPage()

      disclaimerPage goBack() must beAnInstanceOf[G6ApprovePage]
    }

  } section("integration", models.domain.DisclaimerSection.id)
}