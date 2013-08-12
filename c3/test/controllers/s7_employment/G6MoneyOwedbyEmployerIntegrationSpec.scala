package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._

class G6MoneyOwedbyEmployerIntegrationSpec extends Specification with Tags {
  "Money owed to you by your employer" should {
    "be presented" in new WithBrowser with G5AdditionalWageDetailsPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[G6MoneyOwedByEmployerPage]
    }

    "contain 1 completed form" in new WithBrowser with G5AdditionalWageDetailsPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val g6 = page submitPage()

      g6 goToThePage()
      g6 fillPageWith claim
      g6 submitPage() match {
        case p: G7PensionSchemesPage => p numberSectionsCompleted()  mustEqual 2
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G5AdditionalWageDetailsPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G5AdditionalWageDetailsPage]
    }
  } section("integration",models.domain.Employed.id)
}