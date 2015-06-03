package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeApplication
import utils.{LightFakeApplication, WithBrowser}
import utils.pageobjects.TestData
import utils.pageobjects.s0_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPageContext
import scala.language.existentials


class FullSubmissionSpec extends Specification with MockInjector with Tags {
  sequential

  "The application" should {

    "Successfully run claim submission " in new WithBrowser(app = LightFakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "GOOD_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, "/async-submitting", waitForPage = true, waitDuration = 500, trace = false)
    }

    "Successfully run circs submission " in new WithBrowser(app = LightFakeApplication(withGlobal = Some(global))) with G1ReportAChangeInYourCircumstancesPageContext {
      txnId = "GOOD_SUBMIT"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, "/circs-async-submitting", waitForPage = true, waitDuration = 500, trace = true)
    }
  } section "functional"
}