package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.PageObjects

class G8PensionAndExpensesIntegrationSpec extends Specification with Tags {
  "Pension And Expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G8PensionAndExpensesPage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G8PensionAndExpensesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G2BeenEmployedPage => p numberSectionsCompleted()  mustEqual 2
        case _ => ko("Next Page is not of the right type.")
      }
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