package services.submission

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeApplication, WithBrowser}
import utils.pageobjects.TestData
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPageContext
import scala.language.existentials
import submission.MockWebServiceClient


class FullSubmissionSpec extends Specification with MockInjector with Tags {
  sequential

  override val mockSubmission = new MockWebServiceClient

  val claimThankYouPageTitle = "Application complete"
  val changeThankYouPageTitle = "Change complete"
  val cAndDError = "An unrecoverable error has occurred"

  "The application" should {

    "Successfully run claim submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "GOOD_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, claimThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Successfully run circs submission " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1ReportAChangeInYourCircumstancesPageContext {
      txnId = "GOOD_SUBMIT"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, changeThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = true)
    }

    "Unrecoverable Error claim submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "ERROR_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Unrecoverable Error circs submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1ReportAChangeInYourCircumstancesPageContext {
      txnId = "ERROR_SUBMIT"
      val circs = TestData.readTestDataFromFile("/functional_scenarios/circumstances/TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(circs, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Recoverable acknowledgement submission" in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "RECOVER_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, claimThankYouPageTitle, waitForPage = true, waitDuration = 500, trace = false)
    }

    "Handle a unknown response " in new WithBrowser(app = FakeApplication(withGlobal = Some(global))) with G1BenefitsPageContext {
      txnId = "UNKNOWN_SUBMIT"
      val claim = TestData.readTestDataFromFile("/functional_scenarios/ClaimScenario_TestCase1.csv")
      page goToThePage(waitForPage = true, waitDuration = 500)
      val lastPage = page runClaimWith(claim, cAndDError, waitForPage = true, waitDuration = 500, trace = false)
    }

  } section "functional"
}