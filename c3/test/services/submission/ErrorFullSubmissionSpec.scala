package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.TestData
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPageContext
import submission.ErrorMockWebServiceClient

class ErrorFullSubmissionSpec extends Specification with MockInjector with Tags {
  sequential

  override val mockSubmission = new ErrorMockWebServiceClient

  val claimThankYouPageTitle = "Application complete"
  val changeThankYouPageTitle = "Change complete"
  val cAndDError = "An unrecoverable error has occurred"

  val retryClaim = "Try claim again"
  val retryChange = "Try change again"

  "The application" should {

    "Handle a connect exception and redirect to the retry page for a claim" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "CONNECT_EXCEPTION"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, retryClaim, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a connect exception and redirect to the retry page for a change" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1ReportAChangeInYourCircumstancesPageContext {
      txnId = "CONNECT_EXCEPTION"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, retryChange, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a Bad request" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "BAD_REQUEST"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a request timeout" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "TIMEOUT_REQUEST"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a internal server error" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "INTERNAL_ERROR"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a transaction id missing exception" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "TRANSACTION_ID_EXCEPTION"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a exception" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "EXCEPTION"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

  } section "functional"
}
