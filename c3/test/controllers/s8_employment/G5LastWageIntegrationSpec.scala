package controllers.s8_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_employment._
import utils.pageobjects.PageObjects

class G5LastWageIntegrationSpec extends Specification with Tags {
  "Last wage" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G5LastWagePage(context)
      page goToThePage()
    }

    "employer owes you money is not visible when 'have finished job is 'no'" in new WithBrowser with PageObjects{
      val jobDetailsPage = G3JobDetailsPage(context)
      jobDetailsPage goToThePage()
      val claim = ClaimScenarioFactory s7EmploymentWhenFinishedJobNo()
      jobDetailsPage fillPageWith claim
      val lastWagePage = jobDetailsPage submitPage()
      context.browser.find("#employerOwesYouMoney").size() mustEqual 0
    }

    "be able to navigate back" in new WithBrowser  with PageObjects{
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