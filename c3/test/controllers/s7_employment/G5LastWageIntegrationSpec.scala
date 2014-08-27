package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.PageObjects

class G5LastWageIntegrationSpec extends Specification with Tags {
  "Last wage" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G5LastWagePage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G5LastWagePage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G8PensionAndExpensesPage => p numberSectionsCompleted()  mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "contain 1 completed form when 'have finished job is 'yes'" in new WithBrowser with PageObjects{
      val jobDetailsPage = G3JobDetailsPage(context)
      jobDetailsPage goToThePage()
      val claim = ClaimScenarioFactory s7Employment()
      jobDetailsPage fillPageWith claim
      val lastWagePage = jobDetailsPage submitPage()
      lastWagePage fillPageWith claim
      lastWagePage.readYesNo("#employerOwesYouMoney") mustEqual Some("yes")
    }

    "employer owes you money is not visible when 'have finished job is 'no'" in new WithBrowser with PageObjects{
      val jobDetailsPage = G3JobDetailsPage(context)
      jobDetailsPage goToThePage()
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentHaveYouFinishedThisJob_1 = "no"
      jobDetailsPage fillPageWith claim
      val lastWagePage = jobDetailsPage submitPage()
      context.browser.find("#employerOwesYouMoney").size() mustEqual 0
    }

    "be able to navigate back to a completed form" in new WithBrowser  with PageObjects{
			val page =  G3JobDetailsPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G3JobDetailsPage]
    }
  } section("integration",models.domain.Employed.id)
}