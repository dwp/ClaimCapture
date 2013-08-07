package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment.{G2SelfEmploymentYourAccountsPage, G2SelfEmploymentYourAccountsPageContext}
import utils.pageobjects.ClaimScenario
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPage

class G2SelfEmploymentYourAccountsIntegrationSpec extends Specification with Tags {

  "Self Employment - Your Accounts" should {
    "be presented" in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage(waitForPage = true, waitDuration = 500)
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true, waitDuration = 500)

      val nextPage = page goToPage( throwException = false, page = new G2SelfEmploymentYourAccountsPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis = ""
        page goToThePage(waitForPage = true, waitDuration = 500)
        page fillPageWith claim
        val pageWithErrors = page.submitPage(waitForPage = true, waitDuration = 500)
        pageWithErrors.listErrors.size mustEqual 1
      }

      "your accounts invalid date" in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
        val claim = new ClaimScenario
        claim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis = "yes"
        claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "no"
        claim.SelfEmployedTellUsWhyandWhentheChangeHappened = "A Year back"
        claim.SelfEmployedDoYouHaveAnAccountant = "yes"
        claim.SelfEmployedCanWeContactYourAccountant = "yes"
        claim.SelfEmployedWhatWasIsYourTradingYearfrom = "01/01/0000"
        page goToThePage(waitForPage = true, waitDuration = 500)
        page fillPageWith claim
        val pageWithErrors = page.submitPage(waitForPage = true, waitDuration = 500)
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("date")
      }
    }

    "your accounts tell us what happened not required if incoming and outgoing are current " in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
      val claim = new ClaimScenario
      claim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis = "yes"
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      claim.SelfEmployedDoYouHaveAnAccountant = "yes"
      claim.SelfEmployedCanWeContactYourAccountant = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 0
    }

    "your accounts contact your accountant is not required if there is no accountant " in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
      val claim = new ClaimScenario
      claim.SelfEmployedAreTheseAccountsPreparedonaCashFlowBasis = "yes"
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      claim.SelfEmployedDoYouHaveAnAccountant = "no"
      page goToThePage(waitForPage = true, waitDuration = 500)
      page fillPageWith claim
      val pageWithErrors = page.submitPage(waitForPage = true, waitDuration = 500)
      pageWithErrors.listErrors.size mustEqual 0
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage(waitForPage = true, waitDuration = 500)
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G2SelfEmploymentYourAccountsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage(waitForPage = true, waitDuration = 500)
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G2SelfEmploymentYourAccountsPage])
    }
  } section "integration"
}
