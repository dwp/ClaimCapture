package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.PageObjects

class G7PensionSchemesIntegrationSpec extends Specification with Tags {
  "Pension schemes - Integrations" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G7PensionSchemesPage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G7PensionSchemesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G8AboutExpensesPage => p numberSectionsCompleted() mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser with PageObjects{
			val page =  G5LastWagePage(context)
      val claim = ClaimScenarioFactory s7Employment()
      val url = routes.G5LastWage.present(page.iteration.toString).url
      browser.goTo(url)
      browser.click("#anyOtherMoney_no")
      browser.click("#employerOwesYouMoney_yes")
      browser.submit("button[type='submit']")
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G5LastWagePage]
    }
  } section("integration", models.domain.Employed.id)
}