package controllers.s_employment

import org.specs2.mutable._
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_employment._
import utils.pageobjects.PageObjects
import utils.pageobjects.your_income.GYourIncomePage

class GPensionAndExpensesIntegrationSpec extends Specification {
  section("integration",models.domain.PensionAndExpenses.id)
  "Pension And Expenses" should {
    "be presented" in new WithBrowser with PageObjects {
			val page =  GPensionAndExpensesPage(context)
      page goToThePage()
    }

    "be able to navigate back to a completed form" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => { testData.YourIncomeNone = "true" })
			val page =  GPensionAndExpensesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      println(page.source)
      val submitted = page submitPage()
      println(submitted.source)
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[GYourIncomePage]
    }
  }
  section("integration",models.domain.PensionAndExpenses.id)
}
