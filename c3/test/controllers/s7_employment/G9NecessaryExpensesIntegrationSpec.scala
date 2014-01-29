package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._

class G9NecessaryExpensesIntegrationSpec extends Specification with Tags {
  "Necessary Expenses" should {
    "be presented" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "contain 1 completed form" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p fillPageWith claim

      p submitPage() should beLike { case p: G9NecessaryExpensesPage => p numberSectionsCompleted() shouldEqual 1 }
    }

    "be able to navigate back to a completed form" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p goBack() must beAnInstanceOf[G8AboutExpensesPage]
    }
  } section("integration", models.domain.Employed.id)
}