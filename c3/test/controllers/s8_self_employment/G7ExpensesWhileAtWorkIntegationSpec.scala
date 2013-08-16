package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.{G8AboutYouCompletedPage, G4ClaimDatePageContext}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage

class G7ExpensesWhileAtWorkIntegationSpec extends Specification with Tags {

  "Expenses related to the Person you care for while at work" should {
    "be presented" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage(waitForPage = true, waitDuration = 1000)
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true, waitForPage = true, waitDuration = 1000)

      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage(waitForPage = true, waitDuration = 1000)
      page runClaimWith (claim, G8AboutYouCompletedPage.title, waitForPage = true, waitDuration = 1000)

      val nextPage = page goToPage( throwException = false, page = new G7ExpensesWhileAtWorkPage(browser), waitForPage = true, waitDuration = 1000)
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage(waitForPage = true, waitDuration = 1000)
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true, waitForPage = true, waitDuration = 1000)

      page goToThePage(waitForPage = true, waitDuration = 1000)
      page fillPageWith claim
      val g2 = page submitPage(waitForPage = true, waitDuration = 1000)
      val g7 = g2 goToPage (new G7ExpensesWhileAtWorkPage(browser), waitForPage = true, waitDuration = 1000)
      g7.listCompletedForms.size mustEqual 2
    }

    " contain errors on invalid submission missing madatory field" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claim = new ClaimScenario

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage(waitForPage = true, waitDuration = 1000)
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true, waitForPage = true, waitDuration = 1000)


      claim.SelfEmployedCareExpensesNameOfPerson = ""
      page goToThePage (waitForPage = true, waitDuration = 1000)
      page fillPageWith claim
      val pageWithErrors = page.submitPage(waitForPage = true, waitDuration = 1000)
      pageWithErrors.listErrors.size mustEqual 5
      pageWithErrors.listErrors(0).contains("nameOfPerson")
    }
    
    "navigate back to previous page" in new WithBrowser with G6ChildcareProvidersContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage(waitForPage = true, waitDuration = 1000)
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true, waitForPage = true, waitDuration = 1000)

      page goToThePage(waitForPage = true, waitDuration = 1000)
      page fillPageWith claim
      val g7 = page submitPage(waitForPage = true, waitDuration = 1000)
      g7.goBack() must beAnInstanceOf[G6ChildcareProvidersContactDetailsPage]
    }
    
    "navigate to next page on valid submission" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage(waitForPage = true, waitDuration = 1000)
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true, waitForPage = true, waitDuration = 1000)

      page goToThePage(waitForPage = true, waitDuration = 1000)
      page fillPageWith claim

      val nextPage = page submitPage(waitForPage = true, waitDuration = 1000)

      nextPage must not(beAnInstanceOf[G7ExpensesWhileAtWorkPageContext])
    }
  } section("integration", models.domain.SelfEmployment.id)
}