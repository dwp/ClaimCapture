package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.PageObjects

class G6AdditionalWageDetailsIntegrationSpec extends Specification with Tags {
  "Additional wage details" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G6AdditionalWageDetailsPage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G6AdditionalWageDetailsPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim

      page submitPage() should beLike { case p: G7PensionSchemesPage => p numberSectionsCompleted() shouldEqual 1 }
    }

    "be able to navigate back to a completed form" in new WithBrowser with PageObjects{
			val page =  G5LastWagePage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G5LastWagePage]
    }
  } section("integration", models.domain.Employed.id)
}