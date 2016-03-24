package services.submission

import org.specs2.mutable._
import utils.{LightFakeApplication, WithBrowser}
import utils.pageobjects.TestData
import utils.pageobjects.s_eligibility.GBenefitsPageContext
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPageContext
import scala.language.existentials

class FullSubmissionSpec extends Specification {
  sequential

  var txnId: String = ""

  section("functional")
  "The application" should {
    "Successfully run claim submission " in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("submit.prints.xml" -> "false"))) with GBenefitsPageContext {
      txnId = "GOOD_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, "/async-submitting", waitForPage = true, waitDuration = 500, trace = false)
    }

    "Successfully run circs submission " in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("submit.prints.xml" -> "false"))) with GCircsYourDetailsPageContext {
      txnId = "GOOD_SUBMIT"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, "/circs-async-submitting", waitForPage = true, waitDuration = 500, trace = true)
    }
  }
  section("functional")
}
