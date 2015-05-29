package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.TestData
import utils.pageobjects.s0_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.circumstances.s1_start_of_process.G2ReportAChangeInYourCircumstancesPageContext
import scala.language.existentials


class FullSubmissionSpec extends Specification with MockInjector with Tags {
  sequential

  "The application" should {

    "Successfully run claim submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "GOOD_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, "/async-submitting", waitForPage = true, waitDuration = 500, trace = false)
    }

    "Successfully run circs submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G2ReportAChangeInYourCircumstancesPageContext {
      txnId = "GOOD_SUBMIT"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, "/circs-async-submitting", waitForPage = true, waitDuration = 500, trace = true)
    }
  } section "functional"
}