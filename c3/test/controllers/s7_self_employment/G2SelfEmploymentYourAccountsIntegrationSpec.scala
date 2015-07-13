package controllers.s7_self_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.s7_self_employment.{G2SelfEmploymentYourAccountsPage, G2SelfEmploymentYourAccountsPageContext}
import utils.pageobjects.{PageObjects, TestData}
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s8_employment.G1EmploymentPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePageContext

class G2SelfEmploymentYourAccountsIntegrationSpec extends Specification with Tags {

  "Self Employment - Your Accounts" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with G1ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val employmentHistoryPage = page runClaimWith(claim, G1EmploymentPage.url, waitForPage = true)
      employmentHistoryPage fillPageWith(claim)

      val nextPage = employmentHistoryPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {

      "your accounts invalid date" in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
        val claim = new TestData
        claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "no"
        claim.SelfEmployedTellUsWhyandWhentheChangeHappened = "A Year back"
        claim.SelfEmployedWhatWasIsYourTradingYearfrom = "01/01/0000"
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("date")
      }
    }

    "your accounts tell us what happened not required if incoming and outgoing are current " in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
      val claim = new TestData
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 0
    }

    "your accounts contact your accountant is not required if there is no accountant " in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
      val claim = new TestData
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 0
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G2SelfEmploymentYourAccountsPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G2SelfEmploymentYourAccountsPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}