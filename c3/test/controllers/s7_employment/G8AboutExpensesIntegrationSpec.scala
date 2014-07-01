package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.PageObjects

class G8AboutExpensesIntegrationSpec extends Specification with Tags {
  "About expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G2BeenEmployedPage => p numberSectionsCompleted()  mustEqual 2
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with PageObjects{
			val page =  G7PensionSchemesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G7PensionSchemesPage]
    }
  } section("integration",models.domain.Employed.id)
}