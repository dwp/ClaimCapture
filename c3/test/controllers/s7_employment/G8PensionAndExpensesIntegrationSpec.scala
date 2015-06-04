package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.{Page, PageObjects}

class G8PensionAndExpensesIntegrationSpec extends Specification with Tags {
  "Pension And Expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G8PensionAndExpensesPage(context)
      page goToThePage()
    }

    // navigate to next page - been employed table summary - on submit

    "be able to navigate back to a completed form" in new WithBrowser  with PageObjects{
			val page =  G8PensionAndExpensesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G8PensionAndExpensesPage]
    }
  } section("integration",models.domain.Employed.id)
}