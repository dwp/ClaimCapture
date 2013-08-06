package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario}

class FullSubmissionManual extends Specification with Tags {

  "The application " should {
    "Successfully run absolute Test Case 2 " in new WithBrowser with G1BenefitsPageContext {
      pending("run manually")
      val claim = ClaimScenario.buildClaimFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage()
      val lastPage = page runClaimWith(claim, XmlPage.title, waitForPage = true, waitDuration = 500, trace = false)
      println(lastPage.source())
    }
  }
}
