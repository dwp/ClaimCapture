package controllers.s_self_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.s_self_employment.{GSelfEmploymentYourAccountsPage, GSelfEmploymentYourAccountsPageContext}
import utils.pageobjects.{PageObjects, TestData}
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s_employment.GEmploymentPage
import utils.pageobjects.s_claim_date.GClaimDatePageContext

class GSelfEmploymentYourAccountsIntegrationSpec extends Specification with Tags {

  "Self Employment - Your Accounts" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentYourAccountsPage(context)
      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with GClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val employmentHistoryPage = page runClaimWith(claim, GEmploymentPage.url, waitForPage = true)
      employmentHistoryPage fillPageWith(claim)

      val nextPage = employmentHistoryPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {

      "your accounts invalid date" in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentYourAccountsPage(context)
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
			val page =  GSelfEmploymentYourAccountsPage(context)
      val claim = new TestData
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 0
    }

    "your accounts contact your accountant is not required if there is no accountant " in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentYourAccountsPage(context)
      val claim = new TestData
      claim.SelfEmployedAretheIncomeOutgoingSimilartoYourCurrent = "yes"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 0
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentYourAccountsPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  GSelfEmploymentYourAccountsPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmploymentYourAccounts
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[GSelfEmploymentYourAccountsPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}